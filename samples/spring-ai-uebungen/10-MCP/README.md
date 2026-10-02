# 10-MCP

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8090/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `McpClientController` | `GET /api/mcp-client` | McpClient |
| `McpResourcesController` | `GET /api/mcp-resources` | McpResources |
| `McpPromptsController` | `POST /api/mcp-prompts` | McpPrompts |
| `McpToolsController` | `POST /api/mcp-tools` | McpTools |
| `McpServerToolsController` | `GET /api/mcp-server-tools` | REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar |
| `McpServerResourcesController` | `GET /api/mcp-server-resources` | REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar |
| `McpAnnotationsController` | `POST /api/mcp-annotations` | REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar |

Server in Terminal 1: `mvn spring-boot:run -Dspring-boot.run.profiles=mcp-server` (Port 8090).
Client in Terminal 2: `mvn spring-boot:run -Dspring-boot.run.profiles=mcp-client` (Port 8190).
Die Client-Controller erst nach Start des Servers nutzen. REST-Server-Controller sind direkte Einstiege; MCP verwendet zusätzlich den Streamable-HTTP-Transport unter `/mcp`.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
