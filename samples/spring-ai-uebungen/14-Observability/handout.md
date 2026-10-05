# Handout: Observability

*Projekt 14-Observability*

Dieses Handout erläutert die 5 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8094. Swagger UI: http://localhost:8094/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

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

Actuator exponiert health, metrics und prometheus. Das Profil prompt-logging aktiviert Inhaltsprotokollierung. Ein eigener Trace-Collector wird von den Controllern nicht eingerichtet.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prompt-logging
```

## Überblick über die Lernfolge

AdvisorObservabilityController – Einen Advisor in der beobachteten Verarbeitungskette vergleichen.

ChatClientObservabilityController – Messdaten auf ChatClient-Ebene beobachten.

ModelObservabilityController – Die direkt instrumentierte Model-Ebene untersuchen.

PromptLoggingController – Inhaltsprotokollierung bewusst aktivieren.

VectorStoreObservabilityController – Retrieval-Messdaten getrennt vom Chat untersuchen.

## 1 AdvisorObservabilityController

POST /api/advisor-observability

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Einen Advisor in der beobachteten Verarbeitungskette vergleichen.

**Didaktischer Sinn **SimpleLoggerAdvisor ergänzt den ChatClient-Aufruf. Dadurch lässt sich der zusätzliche Verarbeitungsschritt gegen den einfachen Controller abgrenzen.

```java
return chatClient.prompt()
.user(message)
.advisors(new SimpleLoggerAdvisor())
.call()
.content();
```

**Gezielte Übung **Vergleiche die Messdaten und den Ausführungspfad mit ChatClientObservability. Aktiviere für Inhaltslogs das vorgesehene Profil.

**Worauf es ankommt **Dieser Controller und PromptLogging verwenden denselben Aufruf. Die unterschiedliche Beobachtung entsteht vor allem aus der Konfiguration.

## 2 ChatClientObservabilityController

POST /api/chat-client-observability

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Messdaten auf ChatClient-Ebene beobachten.

**Didaktischer Sinn **Ein gewöhnlicher Chat-Aufruf bleibt im Controller klein; Auto-Konfiguration und Actuator stellen die Beobachtung bereit.

```java
return chatClient.prompt()
.user(message)
.call()
.content();
```

**Gezielte Übung **Rufe den Endpunkt mehrfach auf und untersuche /actuator/metrics sowie /actuator/prometheus vor und nach den Aufrufen.

**Worauf es ankommt **Metriken und Traces sind nicht dasselbe wie Prompt-Inhaltslogs. Die Verfügbarkeit konkreter Messreihen hängt von Registry und Instrumentierung ab.

## 3 ModelObservabilityController

POST /api/model-observability

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Die direkt instrumentierte Model-Ebene untersuchen.

**Didaktischer Sinn **ChatModel.call wird unmittelbar ausgeführt. Die Übung vergleicht Modellmessung mit dem höherliegenden ChatClient-Aufruf.

```java
return model.call(new org.springframework.ai.chat.prompt.Prompt(message));
```

**Gezielte Übung **Rufe beide Endpunkte mit derselben Frage auf und untersuche die Messreihen im Actuator.

**Worauf es ankommt **ChatClient-spezifische Advisors werden hier umgangen. Ein fehlender ChatClient-Messwert bedeutet nicht, dass kein Modellaufruf stattfand.

## 4 PromptLoggingController

POST /api/prompt-logging

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Inhaltsprotokollierung bewusst aktivieren.

**Didaktischer Sinn **Das Profil prompt-logging setzt DEBUG für SimpleLoggerAdvisor und zusätzliche Beobachtungsoptionen. Die Übung unterscheidet Inhaltslogs von technischen Messdaten.

```java
return chatClient.prompt()
.user(message)
.advisors(new SimpleLoggerAdvisor())
.call()
.content();
```

**Gezielte Übung **Starte mit dem Profil und untersuche Logs einer harmlosen Frage. Vergleiche mit einem Start ohne Profil.

**Worauf es ankommt **Prompts und Antworten können vertrauliche Daten enthalten. Der HTTP-Proxy zeigt eine andere Ebene: die tatsächlich übertragenen Provider-Daten.

## 5 VectorStoreObservabilityController

POST /api/vector-store-observability

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Retrieval-Messdaten getrennt vom Chat untersuchen.

**Didaktischer Sinn **Der Controller führt ausschließlich similaritySearch aus. Er macht Embedding- und Store-Arbeit ohne anschließende Textgenerierung beobachtbar.

```java
return vectorStore.similaritySearch(message);
```

**Gezielte Übung **Indexiere Testdaten in den passenden Store. Rufe die Suche auf und untersuche Actuator-Metriken.

**Worauf es ankommt **Ein ChatClient ist injiziert, wird hier aber nicht verwendet. Eine erfolgreiche Suche benötigt Daten und ein erreichbares Embedding-Modell.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Observability. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
