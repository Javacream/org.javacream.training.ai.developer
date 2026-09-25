# Maven-Musterlösung – Block 7

## Thema

MCP, Security und Guardrails

## Voraussetzungen

- Java 21
- Maven 3.9+
- Umgebungsvariable `OPENAI_API_KEY`

## Start

```bash
export OPENAI_API_KEY="..."
mvn spring-boot:run
```

Unter Windows PowerShell:

```powershell
$env:OPENAI_API_KEY="..."
mvn spring-boot:run
```


Der ausführbare Kern demonstriert Guardrails. Die MCP-Aufgabe ist im Notebook enthalten, da ein produktiver MCP-HTTP-Server zusätzlich eine explizite Sicherheitsgrenze benötigt.

## Hinweis

Die Anwendung ist als Seminar-Musterlösung gedacht. Zugangsdaten, Zugriffskontrolle, Persistenz, Retry-Strategien, Rate Limits und Datenschutz müssen für produktive Systeme projektspezifisch ergänzt werden.
