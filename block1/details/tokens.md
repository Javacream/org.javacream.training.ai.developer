# Tokens, Token-Nutzung und Pricing

## Was ist ein Token?

Large Language Models verarbeiten Text nicht unmittelbar als Wörter oder Zeichen. Der Text wird zunächst durch einen **Tokenizer** in kleinere Einheiten zerlegt – die Tokens.

Ein Token kann beispielsweise ein vollständiges Wort, ein Wortbestandteil, eine Zahl oder ein Satzzeichen sein. Deshalb gilt nicht:

```text
1 Wort = 1 Token
```

Wie ein Text zerlegt wird, hängt vom verwendeten Modell und dessen Tokenizer ab. Auch Sprache, Sonderzeichen und Schreibweise beeinflussen die Anzahl.

Für AI-Anwendungen sind Tokens wichtig, weil sie sowohl den Umfang des Modellkontexts als auch Rechenaufwand und – bei vielen Cloud-Angeboten – den Preis beeinflussen.

## Prompt und Prompt Tokens

Ein **Prompt** ist zunächst die inhaltliche Eingabe bzw. Instruktion, die einer AI-Anwendung oder einem Modell für einen Aufruf zur Verfügung gestellt wird. Ein Prompt kann aus mehreren Nachrichten und zusätzlichen Kontextinformationen bestehen.

**Prompt Tokens** sind dagegen nicht der Prompt selbst, sondern die durch den Tokenizer erzeugten bzw. für die Eingabe gezählten Tokens, die das Modell tatsächlich als Input verarbeitet.

Dazu gehört zunächst die sichtbare Benutzerfrage. In einer realen Spring-AI-Anwendung können aber weitere Inhalte Bestandteil der Modelleingabe sein, zum Beispiel:

- System-Nachrichten,
- Gesprächshistorie aus Chat Memory,
- von Advisors ergänzte Informationen,
- RAG-Kontext aus Dokumenten,
- Tool-Beschreibungen und weitere technische Prompt-Bestandteile.

Damit gilt als wichtige Unterscheidung:

```text
Prompt        = inhaltliche Eingabe / Instruktion
Prompt Tokens = tokenisierte bzw. gezählte Eingabe des Modells
```

Prompt Tokens werden daher häufig auch als **Input Tokens** bezeichnet.

Beispiel:

```text
System: Du bist ein Java-Trainer.
User: Was ist Dependency Injection?
```

Beide Nachrichten tragen zur Eingabe des Modells und damit zur Zahl der Prompt Tokens bei.

## Completion Tokens

**Completion Tokens** sind die Tokens, die das Modell bei der Erzeugung seiner Antwort neu produziert.

Sie werden häufig auch als **Output Tokens** bezeichnet.

Wenn das Modell auf eine kurze Frage eine ausführliche Erklärung generiert, kann die Zahl der Completion Tokens deutlich größer als die Zahl der Prompt Tokens sein.

Wichtig ist die Richtung:

```text
Prompt Tokens       = Eingabe IN das Modell
Completion Tokens   = Ausgabe AUS dem Modell
```

Der Begriff *Completion* stammt aus dem Grundprinzip generativer Sprachmodelle: Das Modell vervollständigt den vorhandenen Kontext Token für Token.

## Total Tokens

Die Gesamtzahl ergibt sich grundsätzlich aus Eingabe und Ausgabe:

```text
Total Tokens = Prompt Tokens + Completion Tokens
```

Beispiel aus Block 1:

```text
Prompt-Tokens:      39
Completion-Tokens: 144
Tokens gesamt:     183
```

Damit wurden 39 Tokens an das Modell übergeben und anschließend 144 neue Tokens erzeugt.

## Warum sind Prompt und Completion Tokens getrennt?

Die Unterscheidung ist aus mehreren Gründen relevant.

Erstens beanspruchen Eingabe und Ausgabe gemeinsam Modellressourcen und Kontext. Eine lange Gesprächshistorie oder umfangreicher RAG-Kontext kann die Prompt-Tokens stark erhöhen.

Zweitens unterscheiden viele kommerzielle Modellanbieter beim **Pricing** zwischen Input und Output. Die beiden Tokenarten können unterschiedliche Preise haben.

Drittens hilft die getrennte Betrachtung bei der Optimierung. Eine Anwendung mit sehr vielen Prompt Tokens hat möglicherweise ein anderes Problem als eine Anwendung, deren Modell unnötig lange Antworten erzeugt.

## Tokens und Kontextfenster

Ein Modell kann nur eine begrenzte Menge Kontext verarbeiten. Vereinfacht konkurrieren dabei Eingabe und erzeugte Ausgabe um den verfügbaren Kontext.

In späteren Seminarblöcken wird das besonders wichtig:

```text
System Prompt
+ User Prompt
+ Conversation Memory
+ RAG-Dokumente
+ Tool-Informationen
+ Modellantwort
= Kontextverbrauch
```

Mehr Kontext ist nicht automatisch besser. Irrelevanter Kontext benötigt Rechenleistung und kann die Qualität einer Antwort sogar verschlechtern.

## Pricing bei Cloud-Modellen

Viele Cloud-Provider berechnen Modellnutzung anhand der verarbeiteten Tokenmenge. Typischerweise werden Preise pro größerer Tokenmenge angegeben, beispielsweise pro eine Million Tokens.

Ein vereinfachtes Preismodell könnte so aussehen:

```text
Input:  2,00 € pro 1.000.000 Tokens
Output: 8,00 € pro 1.000.000 Tokens
```

Dann kann man die Kosten eines Requests getrennt berechnen:

```text
Input-Kosten
= Prompt Tokens / 1.000.000 × Input-Preis

Output-Kosten
= Completion Tokens / 1.000.000 × Output-Preis

Gesamtkosten
= Input-Kosten + Output-Kosten
```

Für beispielsweise 10.000 Prompt Tokens und 2.000 Completion Tokens ergäbe dieses **rein illustrative** Preismodell:

```text
Input:  10.000 / 1.000.000 × 2,00 € = 0,020 €
Output:  2.000 / 1.000.000 × 8,00 € = 0,016 €

Gesamt: 0,036 €
```

Die Zahlen dienen nur zur Erklärung der Berechnung. **Konkrete Preise sind provider-, modell- und zeitabhängig und müssen vor einer realen Kostenrechnung in der jeweils aktuellen Preisliste des Anbieters geprüft werden.**

## Pricing bei Ollama

In unserem Seminar läuft `llama3.2` lokal über Ollama. Deshalb gibt es **keine Abrechnung pro Prompt- oder Completion-Token durch Ollama**.

Das bedeutet jedoch nicht, dass lokale Inferenz ohne Kosten ist. Statt eines Preises pro API-Aufruf entstehen lokale Infrastrukturkosten, zum Beispiel durch:

- CPU- bzw. GPU-Rechenleistung,
- Arbeitsspeicher bzw. GPU-Speicher,
- Stromverbrauch,
- Hardware,
- Betrieb und Administration.

Für das Seminar ist die lokale Ausführung besonders praktisch, weil keine Teilnehmer-Accounts, API-Keys oder nutzungsabhängigen Cloud-Kosten benötigt werden.

## Warum zeigen wir die Tokenzahlen trotzdem?

Auch bei Ollama sind die Werte fachlich relevant. Sie machen sichtbar, wie groß Eingabe und Ausgabe eines Modellaufrufs sind und bereiten auf spätere Themen vor.

Insbesondere bei **Conversation Memory** und **RAG** wird die Zahl der Prompt Tokens steigen, weil zusätzliche Informationen an das Modell übergeben werden. Bei Cloud-Modellen hätte dies zusätzlich direkte Auswirkungen auf die Kosten.

Die Block-1-Anwendungen geben deshalb bewusst nur die didaktisch relevanten Werte aus:

```text
=== Token-Nutzung ===
Prompt-Tokens:      39
Completion-Tokens: 144
Tokens gesamt:     183
```

Provider-spezifische Metadaten wie Request-IDs oder Rate Limits werden an dieser Stelle nicht ausgegeben.

## Merksätze

**Prompt Tokens sind Input Tokens.**

**Completion Tokens sind Output Tokens.**

**Total Tokens ergeben sich aus Prompt und Completion Tokens.**

Bei Cloud-Modellen können Input und Output unterschiedlich bepreist werden. Bei unserem lokalen Ollama gibt es keine Tokenabrechnung, die Tokenzahl bleibt aber für Kontextverbrauch, Performance und die spätere Architektur der Anwendung wichtig.
