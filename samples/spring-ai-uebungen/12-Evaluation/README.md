# 12-Evaluation

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/testing.html](https://docs.spring.io/spring-ai/reference/api/testing.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8092/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `RelevancyEvaluatorController` | `POST /api/relevancy-evaluator` | RelevancyEvaluator |
| `FactCheckingEvaluatorController` | `POST /api/fact-checking-evaluator` | FactCheckingEvaluator |
| `CustomEvaluatorController` | `POST /api/custom-evaluator` | Deterministischer eigener Evaluator; Bewertung ist keine semantische Faktenprüfung |

Relevancy/FactChecking Request ist JSON: `{"question":"Was nutzt das Seminar?","context":"Das Seminar nutzt Ollama.","answer":"Ollama."}`. FactChecking verwendet Context als Document und Answer als Claim, gemäß Reference.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
