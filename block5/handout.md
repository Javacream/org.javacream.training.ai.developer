# Handout Block 5 -- RAG mit Spring AI

> **Technische Basis:** Java 21, Spring Boot 4, Spring AI 2, Maven und
> Ollama. Als Chat-Modell dient `llama3.2`, für Embeddings
> `nomic-embed-text`.

## Lernziel

Dieses Handout vertieft die Inhalte von Block 5. Es dient sowohl zur
Begleitung der Übungen als auch als Nachschlageunterlage nach dem
Seminar.

## 1. Vollständiger Ablauf

RAG kombiniert Retrieval und Generation: Benutzerfrage → semantische
Suche → relevante Chunks → Frage plus Kontext → Chat-Modell → Antwort.
Das Modell wird dabei nicht neu trainiert.

## 2. QuestionAnswerAdvisor

Der Advisor integriert Retrieval in den ChatClient-Ablauf. Damit lässt
sich die in Block 3 eingeführte Advisor-Idee für Wissensanreicherung
wiederverwenden.

## 3. Grounding

Das Modell sollte angewiesen werden, auf Basis des bereitgestellten
Kontextes zu antworten und fehlendes Wissen kenntlich zu machen.
Grounding reduziert Halluzinationen, beseitigt sie aber nicht
vollständig.

## 4. Quellen und Metadaten

Quelleninformationen sollten mitgeführt und bei Bedarf ausgegeben
werden. So lässt sich unterscheiden, was das Modell formuliert hat und
auf welchen Dokumenten die Antwort beruht.

## 5. Qualitätshebel

RAG-Qualität entsteht entlang der Kette Dokumentqualität → Parsing →
Chunking → Embedding → Retrieval → Kontextauswahl → Prompt → Generation.
Eine schlechte Antwort ist deshalb nicht automatisch ein LLM-Problem.

## 6. Debugging

Bei Problemen zuerst Retrieval-Treffer ansehen, dann den tatsächlich
übergebenen Kontext, danach Prompt und Modellantwort. Auch der Fall
'kein ausreichender Treffer' muss ausdrücklich getestet werden.

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
