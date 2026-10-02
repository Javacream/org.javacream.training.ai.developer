# 09-ToolCalling

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/tools.html](https://docs.spring.io/spring-ai/reference/api/tools.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8089/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `SimpleToolController` | `POST /api/simple-tool` | Frage z.B. Welches Datum ist heute? |
| `ToolParametersController` | `POST /api/tool-parameters` | Frage z.B. Addiere 17 und 25; @ToolParam |
| `ToolContextController` | `POST /api/tool-context` | Frage nach der Tenant-ID; Context wird von der Anwendung gesetzt |
| `ToolCallbackController` | `POST /api/tool-callback` | ToolCallback |
| `ToolCallbackProviderController` | `POST /api/tool-callback-provider` | ToolCallbackProvider |
| `ReturnDirectController` | `POST /api/return-direct` | ReturnDirect |
| `ToolErrorHandlingController` | `POST /api/tool-error-handling` | Absichtlicher Tool-Fehler; abhängig vom ExceptionProcessor an Modell zurückgegeben oder HTTP 502 |
| `ToolCallingAdvisorController` | `POST /api/tool-calling-advisor` | ToolCallingAdvisor |
| `UserControlledToolController` | `POST /api/user-controlled-tool` | UserControlledTool |
| `ToolSearchController` | `POST /api/tool-search` | Mit Profil tool-search starten; automatische ToolSearchAdvisor-Registrierung |

ToolSearch: `mvn spring-boot:run -Dspring-boot.run.profiles=tool-search`. Andere Tool-Controller ebenfalls mit Profil ausprobieren und die Advisor-Auswahl beobachten.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
