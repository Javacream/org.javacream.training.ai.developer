# Änderung des Chat-Modells

Das Standard-Chat-Modell und die zugehörigen Beschreibungen sowie Pull-Kommandos wurden auf `llama3.2` geändert. Nach dieser Änderung wurden keine weiteren Prüfungen oder Builds durchgeführt. Die nachfolgenden Prüfergebnisse stammen aus dem früheren Stand.

# Paketumstellung

Die Pakete wurden auf `org.javacream.training.spring.ai.<projektname in Kleinbuchstaben>` umgestellt. Auch die Maven-groupId wurde auf `org.javacream.training.spring.ai` und die artifactId auf den vollständigen Projektnamen mit Nummerierung, beispielsweise `01-ChatClient`, gesetzt. Auf ausdrücklichen Wunsch wurden danach keine weiteren Prüfungen oder Builds durchgeführt. Das folgende Prüfprotokoll bezieht sich auf den Stand vor dieser Umstellung.

# Prüfprotokoll

Stand: 3. Oktober 2026.

## Durchgeführt

- Maven `test-compile` über das Aggregator-POM: BUILD SUCCESS für alle 16 Projekte.
- Produktionsquellen und die drei Test-/Startklassen in 16-Testcontainers kompiliert.
- Java 21, Compile-Target Java 17, Spring AI 2.0.1, Spring Boot 4.0.3.
- 16 eigenständige POMs: artifactId = Projektverzeichnis; requirements.md vorhanden.
- Alle 93 vereinbarten RestController sind jeweils in einer eigenen Java-Datei vorhanden.
- 01-ChatClient gestartet: Swagger UI und /v3/api-docs liefern HTTP 200; neun OpenAPI-Pfade geprüft.
- 10-MCP: Server- und Client-Profil gestartet. Tool-Liste, Resource-Lesen und Prompt-Abruf über Streamable HTTP und die REST-Controller geprüft (jeweils HTTP 200).
- ZIP-Inhalt und Archivintegrität geprüft; Build-Verzeichnisse nicht enthalten.

## Noch lokal mit Infrastruktur ausführen

- Ollama-Modellaufrufe, Streaming, Structured Output und Tool-Auswahl.
- Cassandra-Start über Docker Compose, Vector Search und persistentes Chat Memory.
- Testcontainers-Integrationstest: `mvn -f 16-Testcontainers/pom.xml verify -Pintegration`.
- Modellabhängige Evaluation mit bespoke-minicheck.

Diese Prüfungen konnten hier ohne laufendes Ollama und Docker nicht durchgeführt werden. Kompilierung ist kein Nachweis für die fachliche Qualität oder das Verhalten eines Modells.

Vier dedizierte Model-Controller in 07-Models melden ausdrücklich HTTP 501, weil der gewählte Spring-AI-Ollama-Adapter diese Model-Typen nicht bereitstellt. Das ist eine dokumentierte Provider-Grenze.
