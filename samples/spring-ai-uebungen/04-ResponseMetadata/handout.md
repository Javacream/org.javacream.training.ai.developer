# Handout: ResponseMetadata

*Projekt 04-ResponseMetadata*

Dieses Handout erläutert die 3 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8084. Swagger UI: http://localhost:8084/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

## Überblick über die Lernfolge

GenerationMetadataController – Metadaten einer einzelnen Generation untersuchen.

ResponseMetadataController – Metadaten der gesamten Modellantwort lesen.

UsageMetadataController – Token-Verbrauch gezielt auswerten.

## 1 GenerationMetadataController

POST /api/generation-metadata

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Metadaten einer einzelnen Generation untersuchen.

**Didaktischer Sinn **getResults() wird durchlaufen; jede Generation wird mit text und metadata zurückgegeben. So wird Antwort-Metadaten von generationsbezogenen Daten unterschieden.

```java
return chatClient.prompt()
.user(message)
.call().chatResponse().getResults()
.stream().map(g -> Map.of("text",g.getOutput().getText(),
"metadata",g.getMetadata())).toList();
```

**Gezielte Übung **Untersuche Abschlussgrund und Text jeder Generation. Vergleiche die Struktur mit ResponseMetadata.

**Worauf es ankommt **Die Liste muss nicht mehrere Antworten enthalten. Ollama liefert hier typischerweise eine Generation; der Controller fordert keine zusätzlichen Varianten an.

## 2 ResponseMetadataController

POST /api/response-metadata

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Metadaten der gesamten Modellantwort lesen.

**Didaktischer Sinn **Der Controller extrahiert getMetadata() statt des Antworttextes. Er eröffnet den Blick auf Modellidentifikation und Nutzung, die ein reiner String verbirgt.

```java
return chatClient.prompt()
.user(message)
.call().chatResponse().getMetadata();
```

**Gezielte Übung **Sende eine kurze und eine lange Frage. Vergleiche die Felder mit der RAW-Ollama-Antwort im Proxy.

**Worauf es ankommt **Spring AI vereinheitlicht Metadaten. Verfügbarkeit und Befüllung hängen vom Provider ab; die Rückgabe ist kein unverändertes HTTP-Paket.

## 3 UsageMetadataController

POST /api/usage-metadata

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Token-Verbrauch gezielt auswerten.

**Didaktischer Sinn **Der Controller reduziert die Usage auf promptTokens, completionTokens und totalTokens. Das ergibt eine kompakte technische REST-Antwort.

```java
var usage = chatClient.prompt()
.user(message)
.call().chatResponse().getMetadata().getUsage();
return Map.of("promptTokens",usage.getPromptTokens(),
"completionTokens",usage.getCompletionTokens(), "totalTokens",usage.getTotalTokens());
```

**Gezielte Übung **Vergleiche eine Ein-Satz-Frage mit einer Anfrage nach einer ausführlichen Erklärung. Prüfe Eingabe- und Ausgabe-Tokens getrennt.

**Worauf es ankommt **Token-Zahlen sind keine Wortzahlen. Sie zeigen weder einzelne Token-IDs noch Wahrscheinlichkeiten und sind zwischen verschiedenen Tokenizern nicht direkt vergleichbar.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, ResponseMetadata. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
