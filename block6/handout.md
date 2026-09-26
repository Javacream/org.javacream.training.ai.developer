# Handout Block 6 -- Tool Calling und agentische Workflows

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 6. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Vom Antworten zum Handeln

Beim Tool Calling kann das Modell eine strukturierte Anwendungsfunktion
anfordern. Die Anwendung führt das Tool aus und gibt das Ergebnis an das
Modell zurück.

## 2. Tools definieren

Mit `@Tool` werden geeignete Methoden angeboten; `@ToolParam` beschreibt
Parameter. Gute Beschreibungen sind wichtig, weil das Modell daraus
Auswahl und Argumente ableitet.

## 3. Verantwortlichkeiten

Das Tool übernimmt verlässliche Operationen, Datenzugriffe und
Geschäftsregeln; das LLM interpretiert die Benutzerabsicht und
formuliert Ergebnisse.

## 4. Tool Calling versus Agent

Ein einzelner Tool-Aufruf ist noch kein Agent. Agentische Abläufe
entstehen, wenn das Modell wiederholt entscheidet, Tools verwendet,
Ergebnisse bewertet und weitere Schritte plant.

## 5. Read versus Write

Lesende Tools haben ein anderes Risiko als schreibende oder irreversible
Aktionen. Für kritische Änderungen sind Autorisierung, Validierung,
Benutzerbestätigung und gegebenenfalls Human-in-the-loop erforderlich.

## 6. Fehler und Grenzen

Tool-Parameter sind externe Eingaben und müssen validiert werden.
Schleifen brauchen Abbruchbedingungen. Technische Exceptions sollten
nicht unkontrolliert in den Modellkontext gelangen.

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
