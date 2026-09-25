# Trainerleitfaden – Block 2: ChatClient, Prompts und strukturierte Ausgaben

## Einordnung in den Lernpfad

Dieser Block ist Bestandteil des zweitägigen Spring-AI-Seminars. Die Beispielanwendung wird über die Blöcke hinweg fachlich erweitert; das zu diesem Block gehörende Maven-Projekt ist dennoch eigenständig startbar.

## Lernziel

Die Teilnehmenden können ChatClient-Aufrufe variieren, Prompt Templates verwenden, strukturierte Antworten auf Java-Typen abbilden und synchrone sowie gestreamte REST-Endpunkte bereitstellen.

## Voraussetzungen

- Java 21 und grundlegende Java-Kenntnisse
- Spring Boot und Dependency Injection sind bekannt
- Maven-Grundkenntnisse
- Für Modellaufrufe: gültiger `OPENAI_API_KEY`
- Vorwissen aus den vorhergehenden Blöcken, soweit fachlich erforderlich

## Kerninhalte

- ChatModel versus ChatClient
- Fluent API des ChatClient
- System- und User-Messages
- Prompt Templates und Parameter
- Structured Output mit entity() und Java records
- Provider-native strukturierte Ausgabe als optionale Vertiefung
- Synchrone Aufrufe und Streaming
- REST-Endpunkte, Fehlerbehandlung und saubere Schichten

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

- **System- und User-Prompt trennen:** Baue eine Anfrage mit einer stabilen Systemrolle und variabler User-Frage.
- **Prompt parametrisieren:** Verwende Platzhalter für Thema und Zielgruppe anstatt Strings zusammenzukonkatenieren.
- **Structured Output:** Lass das Modell eine Lernkarte als Java record mit title, summary und drei keyPoints zurückgeben.
- **REST-Endpunkte:** Stelle /api/ask synchron und /api/stream als Streaming-Endpunkt bereit.

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
