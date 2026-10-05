# Evaluation von KI-Anwendungen

Evaluation ist die systematische Bewertung einer KI-Anwendung anhand definierter Anforderungen und Testfälle. Bewertet wird das Verhalten der gesamten Anwendung oder einzelner Komponenten: beispielsweise Suche, Antwortgenerierung oder Werkzeugausführung.

Eine überzeugend klingende Antwort reicht als Qualitätsnachweis nicht aus. Eine Anwendung kann verständlich formulieren und trotzdem eine Frage falsch beantworten, unbelegte Aussagen ergänzen oder notwendige Aktionen auslassen.

## Zuerst die Anforderungen definieren

Vor der Wahl einer Metrik muss klar sein, was eine gute Antwort ausmacht. Für einen Assistenten zu Personalrichtlinien könnten das sein:

- Die Frage wird vollständig beantwortet.
- Die Antwort verwendet die aktuell gültige Richtlinie.
- Jede wesentliche fachliche Aussage ist durch die bereitgestellten Quellen gedeckt.
- Fehlen Informationen, benennt die Anwendung diese Lücke.
- Die Antwort enthält nur Inhalte, die der Benutzer sehen darf.
- Laufzeit und Kosten bleiben innerhalb der festgelegten Grenzen.

Diese Anforderungen werden in konkrete Bewertungskriterien und Testfälle übersetzt.

## Ein Testfall

```json
{
  "question": "Wie viele Tage Sonderurlaub gibt es bei einem privaten Umzug?",
  "context": "Bei einem privaten Umzug wird ein Arbeitstag Sonderurlaub gewährt.",
  "expected_facts": ["Ein Arbeitstag Sonderurlaub"],
  "forbidden_claims": ["Zwei Tage Sonderurlaub"],
  "source_id": "HR-2026-Abschnitt-4"
}
```

Eine Referenz muss nicht zwingend ein exakt erwarteter Antworttext sein. Erwartete Fakten und eine Bewertungsrubrik sind oft geeigneter, weil mehrere Formulierungen korrekt sein können.

## Bewertungsverfahren

| Verfahren | Geeignet für | Grenzen |
|---|---|---|
| Deterministische Prüfung | JSON-Schema, Pflichtfelder, Zahlenbereiche, erlaubte Werte, konkrete Tool-Aufrufe | Erkennt Bedeutung und fachliche Richtigkeit nur eingeschränkt |
| Vergleich mit Referenzen | Erwartete Fakten, Klassifikationen, numerische Ergebnisse oder relevante Dokumente | Benötigt verlässliche Referenzen; sprachliche Varianten müssen berücksichtigt werden |
| Modellgestützte Bewertung | Relevanz, Vollständigkeit, Quellentreue anhand einer Rubrik | Der bewertende LLM kann ebenfalls irren und schwankende Ergebnisse liefern |
| Menschliche Bewertung | Fachlich schwierige Fälle und Kalibrierung automatischer Verfahren | Benötigt Zeit und klare Kriterien für konsistente Urteile |

Ein LLM als Bewerter wird häufig als **LLM-as-a-Judge** bezeichnet. Dabei erhält er Aufgabe, zu bewertende Ausgabe und Kriterien. Seine Bewertungen sollten an menschlichen Urteilen geprüft werden. Ein numerischer Score ist keine automatisch kalibrierte Wahrscheinlichkeit für Richtigkeit.

## Wichtige Qualitätsdimensionen

| Dimension | Leitfrage |
|---|---|
| Correctness | Ist die Antwort fachlich richtig? |
| Relevance | Beantwortet sie die gestellte Frage? |
| Completeness | Enthält sie die erforderlichen Informationen? |
| Faithfulness / Groundedness | Sind ihre Aussagen durch den bereitgestellten Kontext gedeckt? |
| Format Compliance | Entspricht sie dem vereinbarten Ausgabeformat? |
| Tool Success | Wurden notwendige Aktionen korrekt mit passenden Argumenten ausgeführt? |
| Latency und Kosten | Wie lange dauert die Aufgabe und welche Ressourcen werden benötigt? |

Quellentreue und Wahrheit sind verschiedene Kriterien. Eine Antwort kann eine veraltete Quelle korrekt wiedergeben und trotzdem eine heute falsche Aussage enthalten. Umgekehrt kann eine Aussage zufällig richtig sein, obwohl der übergebene Kontext sie nicht belegt.

## Retrieval separat bewerten

Bei einer Anwendung mit Dokumentensuche sind beispielsweise folgende Metriken hilfreich:

| Metrik | Grundidee |
|---|---|
| Precision@k | Welcher Anteil der ersten k Treffer ist relevant? |
| Recall@k | Welcher Anteil aller als relevant bekannten Dokumente wurde in den ersten k Treffern gefunden? |
| MRR | Wie früh erscheint der erste relevante Treffer? |
| nDCG@k | Wie gut ist die Rangfolge, wenn Treffer unterschiedlich relevant sein können? |

Für diese Berechnungen benötigt man passende Relevanzurteile. Modellbasierte Kontextmetriken können zusätzliche Einschätzungen liefern; ihre Definitionen sind werkzeugabhängig und sollten nicht ungeprüft mit klassischen Retrieval-Metriken gleichgesetzt werden.

## Beispiel: zwei getrennte Fehler

Die richtige Richtlinie sagt: „Ein Arbeitstag Sonderurlaub“.

1. Die Suche liefert nur eine alte Regelung mit zwei Tagen. Hier liegt ein Retrieval- oder Aktualitätsproblem vor.
2. Die Suche liefert die aktuelle Regelung, aber das Modell antwortet mit zwei Tagen. Hier liegt ein Generierungsproblem vor.

Ein alleiniger Gesamtscore erkennt möglicherweise beide schlechten Antworten, erklärt aber noch nicht die Ursache. Deshalb sollten Frage, Treffer, Prompt, Antwort, verwendete Versionen und Bewertung nachvollziehbar protokolliert werden.

## Ein praktikabler Evaluationsablauf

1. Repräsentative Aufgaben und Grenzfälle sammeln.
2. Erwartete Fakten, relevante Quellen und Bewertungskriterien festlegen.
3. Einen Ausgangszustand als Baseline messen.
4. Eine Änderung vornehmen, etwa an Modell, Prompt, Chunking oder Retrieval.
5. Alte und neue Variante auf demselben Testbestand vergleichen.
6. Fehlergruppen untersuchen und einzelne Beispiele fachlich prüfen.
7. Nach Einführung laufende Qualitätsmessungen und neue Testfälle ergänzen.

Testfälle sollten auch unbeantwortbare Fragen, falsche Prämissen, leere Suchergebnisse und Berechtigungsgrenzen enthalten. Der zur Optimierung genutzte Bestand sollte von einem unabhängigen Prüfbestand getrennt werden.

## Schwankungen berücksichtigen

KI-Ausgaben und modellgestützte Bewertungen können schwanken. Wiederholte Durchläufe helfen, Stabilität zu beurteilen. Neben Durchschnittswerten sind Verteilungen und Fehlerfälle wichtig: Ein guter Mittelwert kann eine problematische Gruppe verdecken.

Auch eine niedrige Temperature garantiert keine vollständige Reproduzierbarkeit. Modellversion, Laufzeit und Konfiguration sollten für Vergleiche dokumentiert werden.

## Quellen

- [Ragas: Evaluieren anhand eines Testbestands](https://docs.ragas.io/en/v0.1.21/getstarted/evaluation.html)
- [Ragas: Faithfulness und Kontextbezug](https://docs.ragas.io/en/v0.3.9/concepts/metrics/available_metrics/faithfulness/)
- [Ragas: Bewertungsdimensionen](https://docs.ragas.io/en/v0.4.3/howtos/migrations/migrate_from_v03_to_v04/)
