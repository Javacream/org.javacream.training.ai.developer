# Maven-Musterlösung – Block 5

## Thema

RAG mit Spring AI

## Voraussetzungen

- Java 21
- Maven 3.9+

## Lokales Modell mit Ollama

Das Projekt verwendet standardmäßig Ollama; ein Cloud-API-Key ist nicht erforderlich.

Vor dem ersten Start Ollama installieren und starten, danach einmalig:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
```

Chat-Modell: `llama3.2`  
Embedding-Modell: `nomic-embed-text`  
Endpunkt: `http://localhost:11434`

## Start

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
mvn spring-boot:run
```

Unter Windows PowerShell:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
mvn spring-boot:run
```


Dieses Projekt verwendet zusätzlich das portable `spring-ai-vector-store`-Modul mit `SimpleVectorStore`.

## Hinweis

Die Anwendung ist als Seminar-Musterlösung gedacht. Zugangsdaten, Zugriffskontrolle, Persistenz, Retry-Strategien, Rate Limits und Datenschutz müssen für produktive Systeme projektspezifisch ergänzt werden.
