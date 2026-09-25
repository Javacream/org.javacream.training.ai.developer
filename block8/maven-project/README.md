# Maven-Musterlösung – Block 8

## Thema

Evaluation, Observability und integrierte Anwendung

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


Prometheus-Metriken sind unter `/actuator/prometheus` verfügbar, sobald die Anwendung gestartet ist.

## Hinweis

Die Anwendung ist als Seminar-Musterlösung gedacht. Zugangsdaten, Zugriffskontrolle, Persistenz, Retry-Strategien, Rate Limits und Datenschutz müssen für produktive Systeme projektspezifisch ergänzt werden.
