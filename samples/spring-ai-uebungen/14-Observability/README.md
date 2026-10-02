# 14-Observability

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/observability/index.html](https://docs.spring.io/spring-ai/reference/observability/index.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8094/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `ChatClientObservabilityController` | `POST /api/chat-client-observability` | Nach Aufruf /actuator/metrics und /actuator/prometheus ansehen |
| `ModelObservabilityController` | `POST /api/model-observability` | ModelObservability |
| `AdvisorObservabilityController` | `POST /api/advisor-observability` | AdvisorObservability |
| `VectorStoreObservabilityController` | `POST /api/vector-store-observability` | VectorStoreObservability |
| `PromptLoggingController` | `POST /api/prompt-logging` | Mit Profil prompt-logging starten; Inhalte werden ausdrücklich in Logs geschrieben |

Vor dem Start Cassandra gemäß `requirements.md` hochfahren. Den ersten Vector-Store-Aufruf erst durchführen, wenn Ollama auch das Embedding-Modell geladen hat. Für Cassandra-Metadaten ist die Spalte `category` mit Index konfiguriert.

Metriken unter `/actuator/prometheus` und `/actuator/metrics`. Für Inhalte in Logs: Profil `prompt-logging`. Traces werden hier nicht an einen externen Collector exportiert.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
