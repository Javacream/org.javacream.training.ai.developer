# Handout: Testcontainers

*Projekt 16-Testcontainers*

Dieses Handout erläutert die 3 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8096. Swagger UI: http://localhost:8096/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

```bash
ollama pull nomic-embed-text
```

Embedding-Modell und gespeicherte Vektoren müssen zusammenpassen. Bei einem Modellwechsel dürfen Vektoren unterschiedlicher Dimensionen oder Modellräume nicht vermischt werden.

Docker muss erreichbar sein. Testcontainers startet Cassandra mit dynamischem Host-Port; Ollama bleibt auf dem Host. Im normalen Start ist training.cassandra.enabled=false, daher kann der VectorStore fehlen. TestApplication liegt im Test-Classpath und wird über die IDE gestartet; sie bindet ContainerConfiguration ein.

```bash
mvn verify -Pintegration
```

CassandraIntegrationIT prüft über einen REST-Aufruf, dass die Rückgabe id, results und den gespeicherten Text enthält. ContainerConfiguration startet Cassandra 5.0.6, wartet auf die Startmeldung, erstellt die Session mit getHost()/getMappedPort(9042) und initialisiert den Store.

## Überblick über die Lernfolge

DynamicConnectionController – Dynamisch zugewiesene Containerports verwenden.

IntegrationTestController – Einen End-to-End-Test mit realer Infrastruktur verstehen.

TestcontainerServiceController – Den Lebenszyklus eines testseitig gestarteten Dienstes verstehen.

## 1 DynamicConnectionController

POST /api/dynamic-connection

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Dynamisch zugewiesene Containerports verwenden.

**Didaktischer Sinn **Der Controller legt ein Document ab und sucht es mit topK(1) wieder. Alle drei Controller verwenden denselben Ablauf; der jeweilige Schwerpunkt liegt in ContainerConfiguration, TestApplication oder dem Integrationstest.

```java
var store=vectorStore.getIfAvailable();
if(store==null) throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit
TestApplication starten oder CASSANDRA_ENABLED=true setzen");
var doc=new Document(message);
store.add(List.of(doc));
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Starte TestApplication beziehungsweise mvn verify -Pintegration. Verfolge container.getHost() und getMappedPort(9042); prüfe ID und wiedergefundenen Text.

**Worauf es ankommt **Ohne verfügbaren VectorStore liefert der Endpunkt HTTP 503. Der Test startet Cassandra, nicht Ollama. Die Assertion prüft Rückgabeinhalte, nicht umfassende Suchqualität oder Modellantworten.

## 2 IntegrationTestController

POST /api/integration-test

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Einen End-to-End-Test mit realer Infrastruktur verstehen.

**Didaktischer Sinn **Der Controller legt ein Document ab und sucht es mit topK(1) wieder. Alle drei Controller verwenden denselben Ablauf; der jeweilige Schwerpunkt liegt in ContainerConfiguration, TestApplication oder dem Integrationstest.

```java
var store=vectorStore.getIfAvailable();
if(store==null) throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit
TestApplication starten oder CASSANDRA_ENABLED=true setzen");
var doc=new Document(message);
store.add(List.of(doc));
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Starte TestApplication beziehungsweise mvn verify -Pintegration. Verfolge container.getHost() und getMappedPort(9042); prüfe ID und wiedergefundenen Text.

**Worauf es ankommt **Ohne verfügbaren VectorStore liefert der Endpunkt HTTP 503. Der Test startet Cassandra, nicht Ollama. Die Assertion prüft Rückgabeinhalte, nicht umfassende Suchqualität oder Modellantworten.

## 3 TestcontainerServiceController

POST /api/testcontainer-service

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Den Lebenszyklus eines testseitig gestarteten Dienstes verstehen.

**Didaktischer Sinn **Der Controller legt ein Document ab und sucht es mit topK(1) wieder. Alle drei Controller verwenden denselben Ablauf; der jeweilige Schwerpunkt liegt in ContainerConfiguration, TestApplication oder dem Integrationstest.

```java
var store=vectorStore.getIfAvailable();
if(store==null) throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit
TestApplication starten oder CASSANDRA_ENABLED=true setzen");
var doc=new Document(message);
store.add(List.of(doc));
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Starte TestApplication beziehungsweise mvn verify -Pintegration. Verfolge container.getHost() und getMappedPort(9042); prüfe ID und wiedergefundenen Text.

**Worauf es ankommt **Ohne verfügbaren VectorStore liefert der Endpunkt HTTP 503. Der Test startet Cassandra, nicht Ollama. Die Assertion prüft Rückgabeinhalte, nicht umfassende Suchqualität oder Modellantworten.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, Testcontainers. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
