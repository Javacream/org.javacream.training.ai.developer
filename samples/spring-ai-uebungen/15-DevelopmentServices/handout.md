# Handout: DevelopmentServices

*Projekt 15-DevelopmentServices*

Dieses Handout erläutert die 2 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8095. Swagger UI: http://localhost:8095/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

**Datenbank **Cassandra 5 muss erreichbar sein. Die Projekt-Compose-Datei und Initialisierung sind unabhängig vom HTTP-Proxy. Mehrere Compose-Stacks mit demselben Host-Port 9042 nicht gleichzeitig starten. Für Cassandra mindestens 4 GB RAM vorsehen.

Spring Boot verwendet docker-compose.yml mit lifecycle-management=start-only. Die Session erhält Kontaktpunkte aus CassandraConnectionDetails.

## Überblick über die Lernfolge

DockerComposeController – Entwicklungsinfrastruktur durch Spring Boot verbinden.

ServiceConnectionController – ConnectionDetails von manuellen Host-/Port-Werten unterscheiden.

## 1 DockerComposeController

POST /api/docker-compose

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Entwicklungsinfrastruktur durch Spring Boot verbinden.

**Didaktischer Sinn **Der Controller liest die Cassandra-Version über CqlSession und stellt daneben eine Chat-Antwort bereit. So werden zwei erfolgreiche externe Verbindungen nachweisbar.

```java
return Map.of("release",session.execute("SELECT release_version FROM
system.local").one().getString("release_version"),"answer",chatClient.prompt()
.user(message)
.call()
.content());
```

**Gezielte Übung **Starte das Projekt und vergleiche Containerstatus, release-Version und answer. Beende die Anwendung und prüfe, ob Cassandra weiterläuft.

**Worauf es ankommt **start-only startet Compose-Dienste, stoppt sie beim Beenden aber nicht automatisch. Der Controller selbst startet keinen Container; dies geschieht durch die Boot-Integration.

## 2 ServiceConnectionController

POST /api/service-connection

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **ConnectionDetails von manuellen Host-/Port-Werten unterscheiden.

**Didaktischer Sinn **CassandraConfiguration baut die Session aus CassandraConnectionDetails auf. Dieser Controller verwendet dieselbe Versionsabfrage wie DockerCompose.

```java
return Map.of("release",session.execute("SELECT release_version FROM
system.local").one().getString("release_version"),"answer",chatClient.prompt()
.user(message)
.call()
.content());
```

**Gezielte Übung **Setze einen Haltepunkt in die Konfiguration und prüfe die von Boot gelieferten Kontaktpunkte. Vergleiche mit manuell konfigurierten Projekten.

**Worauf es ankommt **Der Controller-Code gleicht DockerCompose. Der Schwerpunkt liegt auf der Verbindungskonfiguration, nicht auf einer anderen Datenbankoperation.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, DevelopmentServices. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
