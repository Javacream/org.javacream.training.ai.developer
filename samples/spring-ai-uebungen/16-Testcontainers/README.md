# 16-Testcontainers

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/testcontainers.html](https://docs.spring.io/spring-ai/reference/api/testcontainers.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8096/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `TestcontainerServiceController` | `POST /api/testcontainer-service` | TestcontainerService |
| `DynamicConnectionController` | `POST /api/dynamic-connection` | DynamicConnection |
| `IntegrationTestController` | `POST /api/integration-test` | IntegrationTest |

Containerbetrieb: `mvn spring-boot:test-run -Dspring-boot.test-run.main-class=org.javacream.training.spring.ai.testcontainers.TestApplication`. Test: `mvn verify -Pintegration`. `DynamicConnectionController` nutzt den Store mit dynamischem Host/Port aus dem Container. Es ist kein zweiter Container je Controller erforderlich.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
