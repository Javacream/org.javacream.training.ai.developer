# 05-StructuredOutput

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/structured-output.html](https://docs.spring.io/spring-ai/reference/api/structured-output.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8085/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `BeanOutputController` | `POST /api/bean-output` | BeanOutput |
| `ListOutputController` | `POST /api/list-output` | Kommagetrennte Ausgabe über ListOutputConverter |
| `MapOutputController` | `POST /api/map-output` | MapOutput |
| `GenericOutputController` | `POST /api/generic-output` | GenericOutput |
| `SchemaValidationController` | `POST /api/schema-validation` | SchemaValidation |
| `ProviderStructuredOutputController` | `POST /api/provider-structured-output` | ProviderStructuredOutput |

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
