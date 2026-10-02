# 01-ChatClient

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/chatclient.html](https://docs.spring.io/spring-ai/reference/api/chatclient.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8081/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `SimpleChatController` | `POST /api/simple-chat` | SimpleChat |
| `FluentApiController` | `POST /api/fluent-api` | FluentApi |
| `ChatResponseController` | `POST /api/chat-response` | ChatResponse |
| `EntityResponseController` | `POST /api/entity-response` | Filmografie als Java Record; Request z.B. Nenne drei Filme von Tom Hanks |
| `StreamingChatController` | `POST /api/streaming-chat` | StreamingChat |
| `PromptParameterController` | `POST /api/prompt-parameter` | PromptParameter |
| `MessageMetadataController` | `POST /api/message-metadata` | Message Metadata sind Anwendungskontext; sie werden nicht automatisch als Text an Ollama übermittelt |
| `ChatClientDefaultsController` | `POST /api/chat-client-defaults` | ChatClientDefaults |
| `ChatClientMutationController` | `POST /api/chat-client-mutation` | ChatClientMutation |

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
