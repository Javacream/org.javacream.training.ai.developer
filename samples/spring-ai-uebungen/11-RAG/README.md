# 11-RAG

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html](https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8091/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `SimpleRagController` | `POST /api/simple-rag` | SimpleRag |
| `QuestionAnswerAdvisorController` | `POST /api/question-answer-advisor` | QuestionAnswerAdvisor |
| `DocumentRetrievalController` | `POST /api/document-retrieval` | DocumentRetrieval |
| `QueryTransformationController` | `POST /api/query-transformation` | QueryTransformation |
| `QueryExpansionController` | `POST /api/query-expansion` | QueryExpansion |
| `DocumentReaderController` | `POST /api/document-reader` | DocumentReader |
| `DocumentTransformerController` | `POST /api/document-transformer` | DocumentTransformer |
| `DocumentWriterController` | `POST /api/document-writer` | DocumentWriter |
| `EtlPipelineController` | `POST /api/etl-pipeline` | EtlPipeline |

Vor dem Start Cassandra gemäß `requirements.md` hochfahren. Den ersten Vector-Store-Aufruf erst durchführen, wenn Ollama auch das Embedding-Modell geladen hat. Für Cassandra-Metadaten ist die Spalte `category` mit Index konfiguriert.

Zuerst über DocumentWriter oder EtlPipeline ein ausreichend langes Dokument einlesen; danach Retrieval und RAG testen. QueryTransformation und QueryExpansion erzeugen Suchvarianten und führen selbst keine Suche aus.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
