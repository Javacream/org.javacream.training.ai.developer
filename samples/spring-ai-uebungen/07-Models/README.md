# 07-Models

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/index.html](https://docs.spring.io/spring-ai/reference/api/index.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8087/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `ChatModelController` | `POST /api/chat-model` | ChatModel |
| `ChatOptionsController` | `POST /api/chat-options` | ChatOptions |
| `ModelOptionsController` | `POST /api/model-options` | ModelOptions |
| `EmbeddingModelController` | `POST /api/embedding-model` | EmbeddingModel |
| `ImageModelController` | `POST /api/image-model` | Provider-Grenze: dieser dedizierte Model-Typ ist mit dem Ollama-Adapter nicht verfügbar |
| `TranscriptionModelController` | `POST /api/transcription-model` | Provider-Grenze: dieser dedizierte Model-Typ ist mit dem Ollama-Adapter nicht verfügbar |
| `SpeechModelController` | `POST /api/speech-model` | Provider-Grenze: dieser dedizierte Model-Typ ist mit dem Ollama-Adapter nicht verfügbar |
| `ModerationModelController` | `POST /api/moderation-model` | Provider-Grenze: dieser dedizierte Model-Typ ist mit dem Ollama-Adapter nicht verfügbar |

Vier Model-Typen sind mit dem gewählten Ollama-Adapter nicht verfügbar; diese Controller melden HTTP 501. Ihre Aufgabe ist die Untersuchung der Provider-Grenzen, keine externe Installation.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
