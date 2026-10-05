# Handout: VectorStores

*Projekt 13-VectorStores*

Dieses Handout erläutert die 6 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8093. Swagger UI: http://localhost:8093/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

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

AddDocumentsController – Ein Dokument mit ID im Vector Store ablegen.

DeleteDocumentsController – Dokumente über ihre IDs entfernen.

MetadataFilterController – Semantische Suche mit einer exakten Bedingung kombinieren.

SearchRequestController – Ergebniszahl und Ähnlichkeitsschwelle konfigurieren.

SimilaritySearchController – Semantische Suche ohne Generierung durchführen.

VectorMetadataController – Fachliche Metadaten zusammen mit Documents speichern.

## 1 AddDocumentsController

POST /api/add-documents

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Ein Dokument mit ID im Vector Store ablegen.

**Didaktischer Sinn **vectorStore.add berechnet und speichert das Embedding zusammen mit dem Document. Die ID verbindet Speicherung, Suche und Löschung.

```java
var document=new Document(message);
vectorStore.add(List.of(document));
return Map.of("id",document.getId());
```

**Gezielte Übung **Speichere zwei kurze Texte und notiere die IDs. Suche danach mit einer sinngleichen Frage.

**Worauf es ankommt **Neue IDs erlauben mehrere ähnliche Einträge. Ein wiederholter Text ist nicht automatisch dedupliziert; Speicherung ist keine Chat-Antwort.

## 2 DeleteDocumentsController

POST /api/delete-documents

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Dokumente über ihre IDs entfernen.

**Didaktischer Sinn **Der Request-Body wird als ID an delete übergeben. Die Übung unterscheidet Textinhalt und technische Identität.

```java
vectorStore.delete(List.of(message));
return Map.of("deletedId",message);
```

**Gezielte Übung **Lösche eine zuvor notierte ID und wiederhole die Suche.

**Worauf es ankommt **Der Body enthält die ID, nicht den Dokumenttext. deletedId bestätigt den angeforderten Wert; es beweist nicht, dass vorher tatsächlich ein Eintrag existierte.

## 3 MetadataFilterController

POST /api/metadata-filter

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Semantische Suche mit einer exakten Bedingung kombinieren.

**Didaktischer Sinn **FilterExpressionBuilder erzeugt category == Parameterwert und ergänzt die Vektorsuche. So werden fachliche Filter und Ähnlichkeitsranking gemeinsam sichtbar.

```java
return
vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(5).filterExpression(new
FilterExpressionBuilder().eq("category",category).build()).build());
```

**Gezielte Übung **Verwende dieselbe Frage für zwei Kategorien. Lege die Testdokumente zuvor über VectorMetadata an.

**Worauf es ankommt **AddDocuments setzt keine category. Solche Dokumente passen nicht automatisch zum Standardfilter training. Der Filter ersetzt keine serverseitige Zugriffsprüfung.

## 4 SearchRequestController

POST /api/search-request

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Ergebniszahl und Ähnlichkeitsschwelle konfigurieren.

**Didaktischer Sinn **topK und threshold werden aus Query-Parametern in SearchRequest übertragen. Die Übung macht Retrieval-Verhalten parametrisierbar.

```java
return
vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(topK).similarityThreshold(threshold).build());
```

**Gezielte Übung **Variiere topK und threshold bei derselben Frage. Beobachte Ergebniszahl und Trefferqualität.

**Worauf es ankommt **Eine höhere Schwelle kann alle Treffer entfernen. topK ist keine Garantie einer festen Trefferzahl; Scores hängen vom Store und dessen Distanzmaß ab.

## 5 SimilaritySearchController

POST /api/similarity-search

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Semantische Suche ohne Generierung durchführen.

**Didaktischer Sinn **similaritySearch(message) nutzt Standard-Suchparameter. Die Rückgabe macht Dokumente und mögliche Scores direkt untersuchbar.

```java
return vectorStore.similaritySearch(message);
```

**Gezielte Übung **Vergleiche eine wortgleiche Anfrage, eine Paraphrase und eine themenfremde Frage.

**Worauf es ankommt **Ähnlichkeit ist kein Beweis für Relevanz oder Wahrheit. Das Embedding-Modell muss zu den gespeicherten Vektoren passen.

## 6 VectorMetadataController

POST /api/vector-metadata

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Fachliche Metadaten zusammen mit Documents speichern.

**Didaktischer Sinn **category wird beim Document-Aufbau gesetzt und zusammen mit dem Embedding gespeichert. Das verbindet semantischen Inhalt mit einer expliziten Klassifikation.

```java
var document=new Document(message,Map.of("category",category));
vectorStore.add(List.of(document));
return Map.of("id",document.getId(),"metadata",document.getMetadata());
```

**Gezielte Übung **Speichere Texte in zwei Kategorien. Notiere IDs und Metadaten und suche anschließend gefiltert.

**Worauf es ankommt **Kategorie ist ein exakt gespeicherter Wert, keine automatisch vom Modell erkannte Klasse. Cassandra indexiert category im Projektschema.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, VectorStores. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
