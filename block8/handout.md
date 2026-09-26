# Handout Block 8 -- Evaluation, Observability und integrierte Anwendung

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 8. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Teststrategie

Deterministische Bestandteile wie Guardrails, Filter, Tool-Parameter und
Geschäftsregeln werden weiterhin mit klassischen Unit Tests geprüft.
Generierte Antworten benötigen zusätzliche Evaluationsverfahren.

## 2. Qualitätskriterien

Correctness bewertet fachliche Richtigkeit, Relevance die Passung zur
Frage und Faithfulness/Groundedness die Stützung durch bereitgestellten
Kontext. Gerade bei RAG sind diese Kriterien wichtiger als reine
Textähnlichkeit.

## 3. Evaluatoren

Evaluation betrachtet Frage, Antwort und gegebenenfalls Kontext oder
Referenzdaten. Auch LLM-basierte Evaluatoren sind nicht vollkommen
objektiv und sollten mit repräsentativen Testfällen und fachlicher
Kontrolle kombiniert werden.

## 4. BLEU und ROUGE

Diese Metriken messen stark die textuelle Übereinstimmung und sind für
offene Chat- oder RAG-Antworten oft nur begrenzt aussagekräftig.
Semantisch gleichwertige Antworten können sehr unterschiedlich
formuliert sein.

## 5. Observability

Wichtige Größen sind Laufzeit, Fehler, Modell- und Tool-Aufrufe,
Token-Nutzung soweit verfügbar und Traces über mehrere Komponenten.
Spring Boot Actuator, Micrometer, OpenTelemetry und Prometheus bilden
dafür die technische Basis.

## 6. Gesamtbild

Die Seminararchitektur verbindet REST API → ChatClient →
Advisors/Memory/RAG → ChatModel/Ollama sowie Tools/MCP. Security,
Evaluation, Observability und klassische Tests liegen als
Querschnittsaufgaben um das Gesamtsystem.

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
