# 13-VectorStores

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/vectordbs/apache-cassandra.html](https://docs.spring.io/spring-ai/reference/api/vectordbs/apache-cassandra.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8093/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `AddDocumentsController` | `POST /api/add-documents` | AddDocuments |
| `SimilaritySearchController` | `POST /api/similarity-search` | SimilaritySearch |
| `SearchRequestController` | `POST /api/search-request` | SearchRequest |
| `VectorMetadataController` | `POST /api/vector-metadata` | VectorMetadata |
| `MetadataFilterController` | `POST /api/metadata-filter` | Cassandra: Kategorie als SchemaColumn indizieren, siehe README |
| `DeleteDocumentsController` | `POST /api/delete-documents` | Request enthält Dokument-ID aus AddDocuments |

Vor dem Start Cassandra gemäß `requirements.md` hochfahren. Den ersten Vector-Store-Aufruf erst durchführen, wenn Ollama auch das Embedding-Modell geladen hat. Für Cassandra-Metadaten ist die Spalte `category` mit Index konfiguriert.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
