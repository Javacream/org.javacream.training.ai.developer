# Trainerleitfaden – Block 1: Generative AI und Spring AI verstehen

## Einordnung in den Lernpfad

Dieser Block ist Bestandteil des zweitägigen Spring-AI-Seminars. Die Beispielanwendung wird über die Blöcke hinweg fachlich erweitert; das zu diesem Block gehörende Maven-Projekt ist dennoch eigenständig startbar.

## Lernziel

Die Teilnehmenden verstehen das Grundmodell generativer AI, die wichtigsten Spring-AI-Abstraktionen und können einen ersten Modellaufruf aus einer Spring-Boot-Anwendung durchführen.

## Voraussetzungen

- Java 21 und grundlegende Java-Kenntnisse
- Spring Boot und Dependency Injection sind bekannt
- Maven-Grundkenntnisse
- Für Modellaufrufe: lokal gestarteter Ollama-Dienst
- Vorwissen aus den vorhergehenden Blöcken, soweit fachlich erforderlich


## Ollama als lokale Modellumgebung

Im Seminar wird **Ollama** als lokale und kostenfreie Modellumgebung verwendet. Dadurch benötigen die Teilnehmenden keinen Cloud-Account und keinen API-Key. Spring AI kommuniziert mit dem lokal laufenden Ollama-Dienst über dessen HTTP-Schnittstelle.

### Installation unter Windows

Ollama muss auf jedem Schulungsrechner installiert sein. Die Windows-Version wird über die offizielle Ollama-Downloadseite installiert.

Nach der Installation sollte ein **neues PowerShell-Fenster** geöffnet werden, damit Änderungen an der `PATH`-Umgebung übernommen werden.

Die Installation lässt sich anschließend prüfen mit:

```powershell
ollama --version
```

Falls der Befehl nicht gefunden wird, prüfen:

```powershell
Get-Command ollama
where.exe ollama
```

In diesem Fall ist entweder Ollama noch nicht installiert oder das Installationsverzeichnis steht noch nicht im `PATH`.

### Ollama starten

Unter Windows wird Ollama nach der normalen Installation üblicherweise als Hintergrundanwendung gestartet. Falls der Ollama-Dienst nicht läuft, kann er aus einer PowerShell heraus gestartet werden:

```powershell
ollama serve
```

Der lokale Ollama-Server ist standardmäßig unter folgender Adresse erreichbar:

```text
http://localhost:11434
```

Die Erreichbarkeit kann beispielsweise im Browser oder mit PowerShell geprüft werden:

```powershell
Invoke-RestMethod http://localhost:11434/api/tags
```

### Modelle für das Seminar laden

Für die Seminarbeispiele werden zwei unterschiedliche Modelle verwendet:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
```

`llama3.2` wird als **Chat-Modell** verwendet. Es verarbeitet die Prompts der Anwendung und erzeugt die Antworten.

`nomic-embed-text` wird als **Embedding-Modell** verwendet. Es erzeugt Vektorrepräsentationen von Texten und wird deshalb insbesondere für Vector Stores und RAG benötigt.

Die Modelle werden beim `pull` auf den lokalen Rechner heruntergeladen. Dieser Schritt sollte wegen Downloadgröße und Netzwerkbelastung **vor dem eigentlichen Seminar** auf allen Schulungsrechnern durchgeführt werden.

Mit

```powershell
ollama list
```

lässt sich kontrollieren, ob beide Modelle vorhanden sind.

Erwartet werden mindestens:

```text
llama3.2
nomic-embed-text
```

### Chat-Modell unabhängig von Spring AI testen

Vor dem Start der Spring-Anwendung sollte zunächst geprüft werden, ob das Chat-Modell direkt über Ollama funktioniert:

```powershell
ollama run llama3.2
```

Anschließend kann beispielsweise eingegeben werden:

```text
Was ist Spring Boot?
```

Mit `/bye` wird die interaktive Sitzung beendet.

Damit lässt sich bei späteren Problemen unterscheiden, ob die Ursache bei **Ollama bzw. dem Modell** oder bei der **Spring-AI-Anwendung** liegt.

### Konfiguration in Spring AI

Die Maven-Projekte des Seminars verwenden den Ollama-Starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```

Die Verbindung wird in `application.properties` konfiguriert:

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

Damit bleibt der Java-Code weitgehend unabhängig vom konkreten Model Provider. Die Anwendung arbeitet beispielsweise mit `ChatClient`, `ChatModel` und `EmbeddingModel`; die Provider-Auswahl erfolgt über Abhängigkeiten und Konfiguration.

### Checkliste vor Seminarbeginn

Vor Beginn des ersten Blocks sollte auf jedem Rechner geprüft werden:

1. `ollama --version` funktioniert.
2. Der Ollama-Dienst läuft.
3. `ollama list` zeigt `llama3.2` und `nomic-embed-text`.
4. `ollama run llama3.2` beantwortet eine Testfrage.
5. Erst danach wird das Spring-AI-Maven-Projekt gestartet.

**Trainerhinweis:** Diese Vorbereitung möglichst nicht erst mit allen Teilnehmenden im Seminar durchführen. Insbesondere das gleichzeitige Herunterladen der Modelle auf vielen Rechnern kann das Schulungsnetz erheblich belasten.


## Kerninhalte

- LLM-Grundmodell: Tokens, Kontextfenster, Prompt, Response und Nichtdeterminismus
- Rolle von Temperatur und Modellparametern
- Spring AI als Abstraktionsschicht zwischen Spring-Anwendung und Model Provider
- ChatModel, ChatClient, Prompt, Message und ChatResponse
- EmbeddingModel, VectorStore, Advisors und Tools als Ausblick
- Spring Boot Starter, Auto-Konfiguration und Maven-BOM
- Provider-Konfiguration und sichere Behandlung von API Keys

## Didaktische Leitidee

Der Block folgt dem Muster **Konzept → kurze Demonstration → eigenständige Veränderung → gemeinsame Auswertung**. Der Trainer sollte nicht nur eine funktionierende API zeigen, sondern jeweils erklären, welche Verantwortung Spring AI übernimmt und welche Verantwortung weiterhin in der Anwendung liegt.

## Einstieg

Beginne mit einer konkreten Problemstellung aus einer realen Anwendung. Frage zunächst nach einer Lösung ohne Spring AI und leite anschließend zur jeweiligen Spring-AI-Abstraktion über. So bleibt sichtbar, dass Spring AI keine eigene KI erzeugt, sondern Modellzugriffe, Prompting, Retrieval, Tools und Betriebsaspekte in das Spring-Ökosystem integriert.

## Erarbeitung

### 1. Begriffe und Verantwortlichkeiten

Arbeite die zentralen Begriffe dieses Blocks zunächst an einem kleinen Ablaufdiagramm heraus. Achte besonders auf die Trennung von Anwendungscode, Spring-AI-Abstraktion und externem Modell bzw. externem System.

### 2. Live-Demonstration

Nutze das Maven-Projekt im Unterverzeichnis `maven-project`. Starte zunächst den Ausgangszustand, führe einen Request aus und ändere anschließend genau eine Eigenschaft. Die Teilnehmenden sollen die Wirkung der Änderung beobachten, bevor weitere Details ergänzt werden.

### 3. Codebesprechung

Besprich nicht nur die API-Aufrufe, sondern auch:
- Bean-Lifecycle und Dependency Injection
- Konfiguration statt hartcodierter Zugangsdaten
- Fehlerfälle und sinnvolle Grenzen
- Testbarkeit
- Welche Teile deterministisch und welche modellabhängig sind

### 4. Eigenständige Übungen

- **Architektur zuordnen:** Ordne ChatClient, ChatModel, Provider und LLM in eine Aufrufkette ein und erkläre die Verantwortlichkeit jeder Schicht.
- **Projekt konfigurieren:** Ergänze eine Spring-Boot-Anwendung um den OpenAI-Starter und konfiguriere den API-Key ausschließlich über eine Umgebungsvariable.
- **Ersten Modellaufruf implementieren:** Erzeuge einen ChatClient und stelle eine kurze fachliche Frage an das Modell.
- **Metadaten untersuchen:** Lass dir zusätzlich zur Textantwort das ChatResponse-Objekt geben und untersuche verfügbare Metadaten.

Die Übungen stehen vollständig im Notebook `uebungen.ipynb`. Das Notebook `musterloesung.ipynb` enthält Lösungsvorschläge und Erklärungen. Die Musterlösung ist bewusst nicht als einzig mögliche Lösung zu verstehen.

## Typische Fehlvorstellungen und Trainerhinweise

- Ein LLM-Aufruf ist kein normaler deterministischer Funktionsaufruf.
- Spring AI abstrahiert Providerdetails, beseitigt aber nicht alle Unterschiede zwischen Modellen.
- API Keys gehören nicht in Quellcode oder Git-Repositories.
- Gute Demo-Ergebnisse sind kein Qualitätsnachweis. Varianten und Fehlerfälle bewusst zeigen.
- Bei jeder neuen Abstraktion fragen: Welches konkrete Problem löst sie?
- Teilnehmer sollten Rückgaben und Metadaten im Debugger bzw. über Logging untersuchen, statt nur den finalen Text anzusehen.

## Gemeinsame Auswertung

Lass mindestens zwei unterschiedliche Lösungsvarianten zeigen. Vergleicht:
1. Lesbarkeit und Verantwortlichkeiten,
2. Robustheit bei unerwarteten Modellantworten,
3. Konfigurierbarkeit,
4. Testbarkeit,
5. Verhalten bei fehlenden Zugangsdaten oder externen Fehlern.

## Abschlusscheck

Am Ende sollen die Teilnehmenden ohne Unterlagen erklären können:
- welche Hauptabstraktionen in diesem Block verwendet wurden,
- wo die Grenze zwischen Anwendung und Spring AI liegt,
- welcher Teil des Ergebnisses deterministisch ist,
- wie sie den gezeigten Ansatz in einer realen Spring-Anwendung einsetzen würden.

## Technischer Stand

Die Materialien sind auf **Spring AI 2.0.1**, **Spring Boot 4.0.0** und **Java 21** ausgerichtet. Bei neueren Versionen sollten insbesondere Starter-Namen, Advisor APIs und MCP APIs gegen die aktuelle Spring-AI-Dokumentation geprüft werden.


## Zwei ausführbare Anwendungen

Block 1 enthält zwei eigenständige Java-Anwendungen im normalen Verzeichnis `src/main/java`:

- `SpringAiBlock1Application` führt genau einen Modellaufruf aus, zeigt Antwort und Metadaten und beendet anschließend den Spring-Kontext.
- `InteractiveSpringAiBlock1Application` liest wiederholt Fragen von der Konsole ein. Nach jeder Frage werden Antwort und Metadaten ausgegeben. Eine **Leerzeile** beendet die Anwendung.

Beide Anwendungen verwenden dieselbe Spring-Boot-Konfiguration in `SpringAiBlock1Configuration`. Dadurch ist klar getrennt, was gemeinsame Konfiguration und was das jeweilige Konsolenprogramm ist.

In der IDE kann einfach die gewünschte `main`-Methode gestartet werden. Es ist kein Maven-Profil und keine spezielle Aufrufoption erforderlich.


> **Vertiefung:** Eine ausführliche Erklärung von Prompt-, Completion- und Total-Tokens sowie deren Bedeutung für Cloud-Pricing befindet sich in `details/tokens.md`.
