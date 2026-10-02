# Spring-AI-Übungen mit Ollama

16 eigenständige Maven-Projekte, 93 thematisch benannte RestController.
Spring AI 2.0.1 / Spring Boot 4.0.3, JDK 17+ (21 empfohlen).
Die Projekte sind Beispielimplementierungen mit Arbeitsaufträgen, keine leeren Controller-Gerüste.

## Einstieg

Projekt auswählen, `requirements.md` lesen und `mvn spring-boot:run` im Projektverzeichnis starten.
Swagger UI läuft auf Port 8081 für 01-ChatClient bis Port 8096 für 16-Testcontainers.
Die groupId lautet `org.javacream.training.spring.ai`; die artifactId entspricht dem vollständigen Projektnamen mit Nummerierung, beispielsweise `01-ChatClient`. Alle Dependencies stehen unter `dependencies`; nur die BOM steht unter `dependencyManagement`.

Alle Projekte zusammen kompilieren: `mvn clean test` im obersten Verzeichnis.
Containerbasierter Integrationstest: `mvn -f 16-Testcontainers/pom.xml verify -Pintegration`.
Die Anwendungen werden einzeln gestartet; das Aggregator-POM startet keine Dienste.

## Infrastruktur

Ollama bleibt lokal auf dem Host. Cassandra 5 startet über die jeweiligen Compose-Dateien.
Die Cassandra-Projekte nutzen dieselben Standardports; nur eine Cassandra-Instanz gleichzeitig starten oder Ports und Konfiguration anpassen. Projekte haben getrennte Compose-Volumes, benutzen innerhalb einer Instanz aber die gemeinsame Übungs-Keyspace `spring_ai` und Tabelle `vectors`.

Cassandra wird für persistentes Chat Memory, RAG, Vector Stores und Infrastruktur-Übungen eingesetzt.
Testcontainers startet Cassandra für den Integrationstest selbst, Ollama muss schon laufen.
MCP wird mit zwei Profilen/Prozessen desselben Maven-Projekts demonstriert.

## Umfang und Grenzen

Bildanalyse nutzt llava:7b, Chat/Tools llama3.2, Embeddings nomic-embed-text.
Dedizierte Image-/Speech-/Transcription-/Moderation-Model-APIs sind mit dem Spring-AI-Ollama-Adapter nicht verfügbar. Vier ausdrücklich gekennzeichnete Controller liefern daher HTTP 501.
AI-Ausgaben können variieren; strukturierte Ausgabe und Tool-Nutzung hängen vom Modell ab.

Die 16 vereinbarten Hauptthemen bleiben erhalten. Provider-Unterseiten werden nicht in separate Projekte aufgeteilt. `manifest.json` enthält die Themenstruktur, `controller-index.json` die REST-Einstiege.
Siehe `VALIDATION.md` für tatsächlich durchgeführte Prüfungen.

ResponseMetadata ist eine eigene didaktische Einheit zum ChatResponse-Abschnitt; die aktuelle Navigation hat dafür keine eigenständige Seite.
