# 02-Advisors

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/advisors.html](https://docs.spring.io/spring-ai/reference/api/advisors.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8082/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `LoggingAdvisorController` | `POST /api/logging-advisor` | LoggingAdvisor |
| `AdvisorParameterController` | `POST /api/advisor-parameter` | AdvisorParameter |
| `AdvisorChainController` | `POST /api/advisor-chain` | AdvisorChain |
| `CustomCallAdvisorController` | `POST /api/custom-call-advisor` | CustomCallAdvisor |
| `AdvisorContextController` | `POST /api/advisor-context` | AdvisorContext |
| `CustomStreamAdvisorController` | `POST /api/custom-stream-advisor` | CustomStreamAdvisor |

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
