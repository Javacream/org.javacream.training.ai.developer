# Maven-Musterlösung – Block 1

## Thema

Generative AI und Spring AI verstehen

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



## Hinweis

Die Anwendung ist als Seminar-Musterlösung gedacht. Zugangsdaten, Zugriffskontrolle, Persistenz, Retry-Strategien, Rate Limits und Datenschutz müssen für produktive Systeme projektspezifisch ergänzt werden.


## Zwei ausführbare Anwendungen

Block 1 enthält zwei eigenständige Java-Anwendungen im normalen Verzeichnis `src/main/java`:

- `SpringAiBlock1Application` führt genau einen Modellaufruf aus, zeigt Antwort und Metadaten und beendet anschließend den Spring-Kontext.
- `InteractiveSpringAiBlock1Application` liest wiederholt Fragen von der Konsole ein. Nach jeder Frage werden Antwort und Metadaten ausgegeben. Eine **Leerzeile** beendet die Anwendung.

Beide Anwendungen verwenden dieselbe Spring-Boot-Konfiguration in `SpringAiBlock1Configuration`. Dadurch ist klar getrennt, was gemeinsame Konfiguration und was das jeweilige Konsolenprogramm ist.

In der IDE kann einfach die gewünschte `main`-Methode gestartet werden. Es ist kein Maven-Profil und keine spezielle Aufrufoption erforderlich.
