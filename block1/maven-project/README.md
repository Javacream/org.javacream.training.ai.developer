# Maven-Musterlösung – Block 1

## Thema

Generative AI und Spring AI verstehen

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



## Hinweis

Die Anwendung ist als Seminar-Musterlösung gedacht. Zugangsdaten, Zugriffskontrolle, Persistenz, Retry-Strategien, Rate Limits und Datenschutz müssen für produktive Systeme projektspezifisch ergänzt werden.
