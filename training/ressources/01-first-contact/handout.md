# Handout Block 1 -- Generative AI und Spring AI verstehen

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 1. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Grundprinzip generativer AI

LLMs erzeugen Antworten tokenweise aus dem bereitgestellten Kontext. Sie
sind keine Wissensdatenbanken und liefern nicht automatisch
deterministische oder sachlich korrekte Ergebnisse. Tokens,
Kontextfenster und Modellparameter bestimmen, wie viel Information
verarbeitet werden kann und wie variabel Antworten ausfallen.

## 2. Spring AI als Abstraktionsschicht

Spring AI integriert Modelle in Spring Boot und trennt Fachlogik vom
konkreten Provider. Zentrale Abstraktionen sind ChatModel für den
Modellzugriff, ChatClient als komfortable Fluent API, Prompt und Message
für Eingaben sowie ChatResponse für Ergebnisse und Metadaten.
EmbeddingModel und VectorStore werden später für RAG benötigt.

## 3. Ollama installieren und starten

Im Seminar verwenden wir Ollama lokal und benötigen deshalb keinen
Cloud-API-Key. Unter Windows wird Ollama über den offiziellen Installer
installiert. Danach eine neue PowerShell öffnen und mit
`ollama --version` prüfen. Falls der Befehl nicht gefunden wird, helfen
`Get-Command ollama` und `where.exe ollama`. Ollama läuft unter Windows
normalerweise im Hintergrund; andernfalls kann der Server mit
`ollama serve` gestartet werden. Standardadresse ist
`http://localhost:11434`. Mit
`Invoke-RestMethod http://localhost:11434/api/tags` lässt sich der
Dienst prüfen.

## 4. Modelle laden

Für Chat verwenden wir `llama3.2`, für Embeddings `nomic-embed-text`.
Einmalig ausführen:

``` powershell
ollama pull llama3.2
ollama pull nomic-embed-text
ollama list
```

Mit `ollama run llama3.2` kann das Chat-Modell unabhängig von Spring AI
getestet werden. Das ist für die Fehlersuche wichtig. Die Modelle
sollten auf Schulungsrechnern vor dem Seminar geladen werden.

## 5. Maven und Konfiguration

Der Provider wird über `spring-ai-starter-model-ollama` eingebunden. Die
Anwendung konfiguriert
`spring.ai.ollama.base-url=http://localhost:11434`,
`spring.ai.ollama.chat.options.model=llama3.2` und für spätere Blöcke
`spring.ai.ollama.embedding.options.model=nomic-embed-text`. Der
Java-Code soll möglichst nur Spring-AI-Abstraktionen kennen.

## 6. Erster ChatClient-Aufruf

Ein typischer Aufruf lautet:

``` java
String answer = chatClient.prompt()
    .user("Erkläre Dependency Injection in drei Sätzen.")
    .call()
    .content();
```

Der Code zeigt den zentralen Gedanken: Die Fachlogik verwendet
ChatClient; Ollama bleibt Infrastruktur und Konfiguration.

## Praktische Orientierung

Arbeiten Sie bei Problemen schrittweise: zuerst Infrastruktur und
Konfiguration prüfen, dann die Spring-AI-Abstraktion, anschließend die
tatsächlich an das Modell übergebenen Daten. Trennen Sie klassische
Anwendungslogik konsequent von generativem Verhalten.

## Merksätze

Die AI-Komponente ist Teil einer normalen Spring-Anwendung und
unterliegt denselben Anforderungen an Struktur, Testbarkeit,
Fehlerbehandlung und Sicherheit. Modellantworten sind grundsätzlich als
nicht vollständig vertrauenswürdige externe Ergebnisse zu behandeln.


## Zwei ausführbare Anwendungen

Block 1 enthält zwei eigenständige Java-Anwendungen im normalen Verzeichnis `src/main/java`:

- `SpringAiBlock1Application` führt genau einen Modellaufruf aus, zeigt Antwort und Metadaten und beendet anschließend den Spring-Kontext.
- `InteractiveSpringAiBlock1Application` liest wiederholt Fragen von der Konsole ein. Nach jeder Frage werden Antwort und Metadaten ausgegeben. Eine **Leerzeile** beendet die Anwendung.

Beide Anwendungen verwenden dieselbe Spring-Boot-Konfiguration in `SpringAiBlock1Configuration`. Dadurch ist klar getrennt, was gemeinsame Konfiguration und was das jeweilige Konsolenprogramm ist.

In der IDE kann einfach die gewünschte `main`-Methode gestartet werden. Es ist kein Maven-Profil und keine spezielle Aufrufoption erforderlich.


> **Vertiefung:** Eine ausführliche Erklärung von Prompt-, Completion- und Total-Tokens sowie deren Bedeutung für Cloud-Pricing befindet sich in `details/tokens.md`.
