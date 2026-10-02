# 15-DevelopmentServices

Eigenständiges Maven-Projekt. Referenz: [https://docs.spring.io/spring-ai/reference/api/docker-compose.html](https://docs.spring.io/spring-ai/reference/api/docker-compose.html).
Spring AI 2.0.1, Spring Boot 4.0.3, Java 17+, Swagger UI.

## Start

1. `requirements.md` beachten.
2. `mvn clean test` (Kompilierung; keine AI-Aufrufe).
3. `mvn spring-boot:run`.
4. Swagger UI: http://localhost:8095/swagger-ui.html

Arbeitsaufträge stehen in `exercises.md`, passende Beispielaufrufe in `requests.http`.

String-Requests als `text/plain` senden. Swagger zeigt die Parameter und Body-Typen.
Für Filmography-Ausgaben z.B. „Nenne drei Filme von Tom Hanks“ verwenden.

## Übungen

| Controller | Endpoint | Lernziel |
|---|---|---|
| `DockerComposeController` | `POST /api/docker-compose` | Compose startet Cassandra; ConnectionDetails siehe Konfiguration |
| `ServiceConnectionController` | `POST /api/service-connection` | Compose startet Cassandra; ConnectionDetails siehe Konfiguration |

Spring Boot startet Compose beim Anwendungsstart und leitet die Cassandra-Verbindung aus `CassandraConnectionDetails` ab. Die ServiceConnection-Übung nutzt denselben automatisch erkannten Service.

## Arbeitsauftrag

Für jeden Controller: Implementierung lesen, Endpoint ausführen und Eingaben variieren. Antwort, Metadaten oder Seiteneffekte überprüfen. Anschließend eine kleine fachliche Erweiterung selbst implementieren. Jeder Controller enthält eine ausführbare Beispielimplementierung; externe Infrastruktur ist Voraussetzung für die entsprechenden Aufrufe.
