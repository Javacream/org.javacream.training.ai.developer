# Handout: RAG

*Projekt 11-RAG*

Dieses Handout erläutert die 9 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8091. Swagger UI: http://localhost:8091/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

```bash
ollama pull nomic-embed-text
```

Embedding-Modell und gespeicherte Vektoren müssen zusammenpassen. Bei einem Modellwechsel dürfen Vektoren unterschiedlicher Dimensionen oder Modellräume nicht vermischt werden.

**Datenbank **Cassandra 5 muss erreichbar sein. Die Projekt-Compose-Datei und Initialisierung sind unabhängig vom HTTP-Proxy. Mehrere Compose-Stacks mit demselben Host-Port 9042 nicht gleichzeitig starten. Für Cassandra mindestens 4 GB RAM vorsehen.

```bash
docker compose up -d --wait
docker compose run --rm cassandra-init
```

## Überblick über die Lernfolge

DocumentReaderController – Rohtext in Spring-AI-Documents überführen.

DocumentRetrievalController – Semantisch passende Documents abrufen.

DocumentTransformerController – Dokumente für die Suche in kleinere Einheiten teilen.

DocumentWriterController – Documents in den Vector Store schreiben.

EtlPipelineController – Lesen, Transformieren und Schreiben zusammensetzen.

QueryExpansionController – Mehrere Suchformulierungen erzeugen.

QueryTransformationController – Eine Suchanfrage vom Modell umformulieren lassen.

QuestionAnswerAdvisorController – Die RAG-Integration in einen Advisor auslagern.

SimpleRagController – Retrieval und Prompt-Erweiterung manuell verbinden.

## 1 DocumentReaderController

POST /api/document-reader

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Rohtext in Spring-AI-Documents überführen.

**Didaktischer Sinn **TextReader liest eine UTF-8-ByteArrayResource und erzeugt Documents. Die Übung isoliert den Extract-Schritt einer ETL-Pipeline.

```java
return new TextReader(new
ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8))).get();
```

**Gezielte Übung **Sende einen mehrzeiligen Text und untersuche ID, Text und Metadaten der Rückgabe.

**Worauf es ankommt **Dieser Reader verarbeitet hier Text, keine PDF- oder Office-Dateien. Er speichert die Documents nicht im Vector Store.

## 2 DocumentRetrievalController

POST /api/document-retrieval

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Semantisch passende Documents abrufen.

**Didaktischer Sinn **VectorStoreDocumentRetriever sucht mit topK(3). Query und Retrieval werden als eigene RAG-Komponenten sichtbar.

```java
return
VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3).build().retrieve(new
Query(message));
```

**Gezielte Übung **Indexiere zuerst Dokumente und frage anschließend mit einer sinngleichen Formulierung.

**Worauf es ankommt **Der Endpunkt liefert Documents, keine ausformulierte Antwort. topK ist eine Obergrenze; passende Daten müssen zuvor vorhanden sein.

## 3 DocumentTransformerController

POST /api/document-transformer

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Dokumente für die Suche in kleinere Einheiten teilen.

**Didaktischer Sinn **TokenTextSplitter zerlegt einen Document-Text mit ChunkSize 100 und kleineren Mindestgrenzen. Dadurch wird Chunking als eigenständiger Verarbeitungsschritt sichtbar.

```java
return
TokenTextSplitter.builder().withChunkSize(100).withMinChunkSizeChars(10).withMinChunkLengthToEmbed(5).build().apply(List.of(new
org.springframework.ai.document.Document(message)));
```

**Gezielte Übung **Sende einen langen Text mit Absätzen. Vergleiche Anzahl und Grenzen der Chunks.

**Worauf es ankommt **ChunkSize ist eine Zielgröße; Grenzregeln beeinflussen die Ausgabe. Kurze Texte können ungeteilt bleiben. Chunking ist noch keine Speicherung.

## 4 DocumentWriterController

POST /api/document-writer

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Documents in den Vector Store schreiben.

**Didaktischer Sinn **Ein Document wird an vectorStore.accept übergeben. Die Übung zeigt den Load-Schritt und gibt die IDs für spätere Abfragen zurück.

```java
var documents=List.of(new Document(message));
vectorStore.accept(documents);
return documents.stream().map(Document::getId).toList();
```

**Gezielte Übung **Speichere eine interne Trainingsregel und suche anschließend danach. Notiere die zurückgegebene ID.

**Worauf es ankommt **Die Speicherung berechnet Embeddings. Der Controller erzeugt selbst keine Chat-Antwort und zerlegt den Text nicht vorher.

## 5 EtlPipelineController

POST /api/etl-pipeline

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Lesen, Transformieren und Schreiben zusammensetzen.

**Didaktischer Sinn **TextReader, TokenTextSplitter und VectorStore werden in einer Sequenz verbunden. So wird aus Einzeloperationen eine Indexierungspipeline.

```java
var reader=new TextReader(new
ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
var
documents=TokenTextSplitter.builder().withChunkSize(100).withMinChunkSizeChars(10).withMinChunkLengthToEmbed(5).build().apply(reader.get());
vectorStore.accept(documents);
return documents.stream().map(Document::getId).toList();
```

**Gezielte Übung **Indexiere einen längeren Trainingsleitfaden. Vergleiche die Zahl der IDs mit dem ungeteilten DocumentWriter.

**Worauf es ankommt **Die Pipeline führt Ingestion aus, nicht Fragebeantwortung. Wiederholte Aufrufe mit neuen Document-IDs können zusätzliche Einträge erzeugen.

## 6 QueryExpansionController

POST /api/query-expansion

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Mehrere Suchformulierungen erzeugen.

**Didaktischer Sinn **MultiQueryExpander fordert drei Query-Varianten an. Unterschiedliche Formulierungen können später verschiedene relevante Dokumente erschließen.

```java
return
MultiQueryExpander.builder().chatClientBuilder(chatClient.mutate()).numberOfQueries(3).build().expand(new
Query(message));
```

**Gezielte Übung **Sende eine Fachfrage und vergleiche die zurückgegebenen Varianten und deren Bedeutungsnähe.

**Worauf es ankommt **Die Varianten werden hier nur erzeugt. Retrieval, Zusammenführung und Deduplizierung sind im Controller nicht implementiert; die Originalfrage kann zusätzlich enthalten sein.

## 7 QueryTransformationController

POST /api/query-transformation

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Eine Suchanfrage vom Modell umformulieren lassen.

**Didaktischer Sinn **RewriteQueryTransformer verwendet einen aus dem ChatClient abgeleiteten Builder. Die Übung trennt Nutzerfrage und optimierte Suchfrage.

```java
return
RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate()).build().transform(new
Query(message));
```

**Gezielte Übung **Sende eine unpräzise Frage mit Füllwörtern. Prüfe, ob die umformulierte Query die ursprüngliche Absicht bewahrt.

**Worauf es ankommt **Der Controller führt weder Retrieval noch eine vollständige RAG-Kette aus. Umformulierung ist selbst ein Modellaufruf und kann die Bedeutung verschieben.

## 8 QuestionAnswerAdvisorController

POST /api/question-answer-advisor

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die RAG-Integration in einen Advisor auslagern.

**Didaktischer Sinn **QuestionAnswerAdvisor kapselt Retrieval und Prompt-Erweiterung mit topK(3). Der Vergleich mit SimpleRag zeigt die Wiederverwendung einer Verarbeitungskomponente.

```java
return chatClient.prompt()
.user(message)
.advisors(QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder().topK(3).build()).build())
.call()
.content();
```

**Gezielte Übung **Verwende dieselben Dokumente und Fragen wie beim manuellen RAG. Vergleiche Requests im Proxy.

**Worauf es ankommt **Der Endpunkt liefert nur content(). Für zusätzliche Retrieval-Kontextdaten ist ChatClientResponse interessant. Indexierung findet in diesem Controller nicht statt.

## 9 SimpleRagController

POST /api/simple-rag

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Retrieval und Prompt-Erweiterung manuell verbinden.

**Didaktischer Sinn **Der Controller sucht drei Documents, verbindet ihre Texte und setzt sie in eine System Message. Der vollständige Ablauf bleibt explizit sichtbar.

```java
var
documents=vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(3).build());
String
context=documents.stream().map(Document::getText).collect(java.util.stream.Collectors.joining("\n"));
return chatClient.prompt()
.system("Antworte ausschließlich mit diesem Kontext. Fehlt die Information, sage
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Speichere eine erfundene interne Regel. Frage danach und danach nach einem nicht vorhandenen Detail.

**Worauf es ankommt **Die Kontextanweisung reduziert unbelegte Antworten, garantiert dies aber nicht. Leere oder unpassende Treffer müssen untersucht werden; Quellen werden nicht automatisch mit ausgegeben.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, RAG. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
