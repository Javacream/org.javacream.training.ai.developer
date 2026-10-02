# 08-ChatMemory

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/chat-memory.html](https://docs.spring.io/spring-ai/reference/api/chat-memory.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8088/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `ChatMemoryController` | `POST /api/chat-memory` | ChatMemory |
| `ConversationMemoryController` | `POST /api/conversation-memory` | ConversationMemory |
| `MessageWindowController` | `POST /api/message-window` | MessageWindow |
| `MemoryAdvisorController` | `POST /api/memory-advisor` | MemoryAdvisor |
| `InMemoryRepositoryController` | `POST /api/in-memory-repository` | InMemoryRepository |
| `PersistentMemoryController` | `POST /api/persistent-memory` | Eigenes Cassandra ChatMemoryRepository; Text-Rollen, keine Tool-Messages; sequentielle Demo-Aufrufe |

Vor dem Start Cassandra gemäß `requirements.md` hochfahren. Den ersten Vector-Store-Aufruf erst durchführen, wenn Ollama auch das Embedding-Modell geladen hat. Für Cassandra-Metadaten ist die Spalte `category` mit Index konfiguriert.

Das eigene `TrainingCassandraChatMemoryRepository` ist bewusst einfach: System/User/Assistant-Text, ohne Media/Tool-Messages und ohne nebenläufige Aktualisierung derselben Conversation. Für diese Übung Aufrufe je Conversation sequentiell durchführen. Nach Neustart dieselbe Conversation-ID verwenden.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
