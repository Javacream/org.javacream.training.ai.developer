# Handout: Models

*Projekt 07-Models*

Dieses Handout erläutert die 8 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8087. Swagger UI: http://localhost:8087/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

```bash
ollama pull nomic-embed-text
```

Embedding-Modell und gespeicherte Vektoren müssen zusammenpassen. Bei einem Modellwechsel dürfen Vektoren unterschiedlicher Dimensionen oder Modellräume nicht vermischt werden.

## Überblick über die Lernfolge

ChatModelController – Die Model-API direkt verwenden.

ChatOptionsController – Portable Optionen pro Anfrage setzen.

EmbeddingModelController – Text in einen numerischen Vektor umwandeln.

ImageModelController – Bildgenerierung als eigene Model-Fähigkeit einordnen.

ModelOptionsController – Provider-spezifische Optionen einsetzen.

ModerationModelController – Moderationsbewertung als eigene Model-Fähigkeit einordnen.

SpeechModelController – Sprachausgabe als eigene Model-Fähigkeit einordnen.

TranscriptionModelController – Audio-Transkription als eigene Model-Fähigkeit einordnen.

## 1 ChatModelController

POST /api/chat-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die Model-API direkt verwenden.

**Didaktischer Sinn **model.call(new Prompt(message)) umgeht die ChatClient-Fluent-API. So wird die Ebene sichtbar, an die der ChatClient normalerweise delegiert.

```java
return model.call(new org.springframework.ai.chat.prompt.Prompt(message));
```

**Gezielte Übung **Vergleiche Antwortstruktur und Aufrufpfad mit SimpleChat. Setze einen Haltepunkt vor model.call.

**Worauf es ankommt **ChatClient-Advisors laufen bei diesem direkten Aufruf nicht automatisch mit. Die Rückgabe ist eine ChatResponse, kein reiner String.

## 2 ChatOptionsController

POST /api/chat-options

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Portable Optionen pro Anfrage setzen.

**Didaktischer Sinn **temperature(0.1) und maxTokens(100) steuern Variabilität und Ausgabelimit über ChatOptions. Die Übung zeigt eine providerübergreifende Oberfläche.

```java
return chatClient.prompt()
.user(message)
.options(ChatOptions.builder().temperature(0.1).maxTokens(100))
.call()
.content();
```

**Gezielte Übung **Fordere eine ausführliche Erklärung an und untersuche, ob das Ausgabelimit die Antwort beendet.

**Worauf es ankommt **Ein kleines Limit kann Antworten abschneiden. Niedrige Temperature garantiert keine Faktenrichtigkeit; Optionen werden vom jeweiligen Adapter übersetzt.

## 3 EmbeddingModelController

POST /api/embedding-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Text in einen numerischen Vektor umwandeln.

**Didaktischer Sinn **embed(message) liefert float[]; Dimension und Werte werden zurückgegeben. Das führt die Grundlage semantischer Suche ohne Chat-Antwort vor.

```java
float[] vector=embeddingModel.embed(message);
return Map.of("dimensions",vector.length,"vector",vector);
```

**Gezielte Übung **Vergleiche Vektoren für ähnliche und unähnliche Sätze. Untersuche die Dimension, statt einzelne Zahlen sprachlich zu interpretieren.

**Worauf es ankommt **nomic-embed-text ist ein separates Modell. Dimension und Vektorraum hängen vom Embedding-Modell ab; unterschiedliche Modelle nicht mischen.

## 4 ImageModelController

POST /api/image-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Bildgenerierung als eigene Model-Fähigkeit einordnen.

**Didaktischer Sinn **Der Endpunkt liefert bewusst HTTP 501. Er verdeutlicht, dass eine gemeinsame Model-Architektur nicht bedeutet, dass jeder Provider jeden Modelltyp unterstützt.

```java
throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_IMPLEMENTED,
"ImageModel: kein dedizierter Spring-AI-Ollama-Adapter. Siehe requirements.md.");
```

**Gezielte Übung **Rufe den Endpunkt auf und untersuche Status und Fehlermeldung. Skizziere, welche Eingabe und Ausgabe ein geeigneter Adapter für diese Aufgabe benötigen würde.

**Worauf es ankommt **Dieser Controller führt keine Bildgenerierung aus. Im Projekt fehlt dafür ein dedizierter Spring-AI-Ollama-Adapter; es wird kein Ergebnis simuliert.

## 5 ModelOptionsController

POST /api/model-options

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Provider-spezifische Optionen einsetzen.

**Didaktischer Sinn **OllamaChatOptions setzt numCtx(4096), numPredict(100) und seed(42). Dadurch werden Optionen sichtbar, die über die portable API hinausgehen.

```java
return chatClient.prompt()
.user(message)
.options(OllamaChatOptions.builder().numCtx(4096).numPredict(100).seed(42))
.call()
.content();
```

**Gezielte Übung **Wiederhole dieselbe Frage und vergleiche mit verändertem Seed. Untersuche die Optionen im Proxy.

**Worauf es ankommt **Ein Seed verbessert Reproduzierbarkeit unter gleichen Bedingungen, garantiert sie aber nicht über unterschiedliche Modellversionen oder Ausführungsumgebungen hinweg.

## 6 ModerationModelController

POST /api/moderation-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Moderationsbewertung als eigene Model-Fähigkeit einordnen.

**Didaktischer Sinn **Der Endpunkt liefert bewusst HTTP 501. Er verdeutlicht, dass eine gemeinsame Model-Architektur nicht bedeutet, dass jeder Provider jeden Modelltyp unterstützt.

```java
throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_IMPLEMENTED,
"ModerationModel: kein dedizierter Spring-AI-Ollama-Adapter. Siehe requirements.md.");
```

**Gezielte Übung **Rufe den Endpunkt auf und untersuche Status und Fehlermeldung. Skizziere, welche Eingabe und Ausgabe ein geeigneter Adapter für diese Aufgabe benötigen würde.

**Worauf es ankommt **Dieser Controller führt keine Moderationsbewertung aus. Im Projekt fehlt dafür ein dedizierter Spring-AI-Ollama-Adapter; es wird kein Ergebnis simuliert.

## 7 SpeechModelController

POST /api/speech-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Sprachausgabe als eigene Model-Fähigkeit einordnen.

**Didaktischer Sinn **Der Endpunkt liefert bewusst HTTP 501. Er verdeutlicht, dass eine gemeinsame Model-Architektur nicht bedeutet, dass jeder Provider jeden Modelltyp unterstützt.

```java
throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_IMPLEMENTED,
"SpeechModel: kein dedizierter Spring-AI-Ollama-Adapter. Siehe requirements.md.");
```

**Gezielte Übung **Rufe den Endpunkt auf und untersuche Status und Fehlermeldung. Skizziere, welche Eingabe und Ausgabe ein geeigneter Adapter für diese Aufgabe benötigen würde.

**Worauf es ankommt **Dieser Controller führt keine Sprachausgabe aus. Im Projekt fehlt dafür ein dedizierter Spring-AI-Ollama-Adapter; es wird kein Ergebnis simuliert.

## 8 TranscriptionModelController

POST /api/transcription-model

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Audio-Transkription als eigene Model-Fähigkeit einordnen.

**Didaktischer Sinn **Der Endpunkt liefert bewusst HTTP 501. Er verdeutlicht, dass eine gemeinsame Model-Architektur nicht bedeutet, dass jeder Provider jeden Modelltyp unterstützt.

```java
throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_IMPLEMENTED,
"TranscriptionModel: kein dedizierter Spring-AI-Ollama-Adapter. Siehe
requirements.md.");
```

**Gezielte Übung **Rufe den Endpunkt auf und untersuche Status und Fehlermeldung. Skizziere, welche Eingabe und Ausgabe ein geeigneter Adapter für diese Aufgabe benötigen würde.

**Worauf es ankommt **Dieser Controller führt keine Audio-Transkription aus. Im Projekt fehlt dafür ein dedizierter Spring-AI-Ollama-Adapter; es wird kein Ergebnis simuliert.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Models. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
