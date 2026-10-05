# RAG – Retrieval-Augmented Generation

RAG verbindet die Suche nach externen Informationen mit der Textgenerierung eines Sprachmodells. Vor der Antwort werden passende Inhalte abgerufen und dem Modell als Kontext übergeben. So kann eine Anwendung aktuelle oder unternehmensinterne Informationen verwenden, die nicht im Trainingswissen des Modells enthalten sind.

## Ein konkretes Beispiel

Ein Mitarbeiter fragt: „Wie viele Tage Sonderurlaub bekomme ich bei einem Umzug?“ Die Anwendung sucht in den gültigen Personalrichtlinien. Sie findet den Absatz „Bei einem privaten Umzug wird ein Arbeitstag Sonderurlaub gewährt“ und übergibt ihn zusammen mit der Frage an das Modell. Das Modell formuliert daraus eine Antwort und kann die Quelle nennen.

Die fachliche Information kommt aus der Richtlinie. Das Sprachmodell übernimmt die Verarbeitung und Formulierung.

## Zwei getrennte Abläufe

### 1. Inhalte vorbereiten: Ingestion und Indexierung

1. Dokumente aus Dateien, Webseiten, Datenbanken oder anderen Quellen laden.
2. Text extrahieren und bereinigen; bei gescannten Dokumenten gegebenenfalls OCR verwenden.
3. Inhalte in geeignete Abschnitte, sogenannte Chunks, zerlegen.
4. Metadaten ergänzen: Quelle, Dokumentversion, Abschnitt, Datum und Zugriffszuordnung.
5. Einen Suchindex erstellen. Für Vektorsuche werden Embeddings berechnet und gespeichert; für Stichwortsuche wird ein entsprechender Textindex aufgebaut.

Diese Schritte laufen typischerweise bei der Aufnahme oder Änderung von Dokumenten, nicht bei jeder Benutzerfrage.

### 2. Eine Frage beantworten

1. Frage und Benutzerberechtigungen entgegennehmen.
2. Passende Inhalte suchen: beispielsweise mit Stichwortsuche, Vektorsuche oder einer Kombination.
3. Kandidaten filtern, zusammenführen und gegebenenfalls durch ein Reranking neu bewerten.
4. Eine begrenzte Auswahl als Kontext zusammenstellen.
5. Frage, Kontext und Antwortanweisungen an das Sprachmodell senden.
6. Antwort und zugehörige Quellen an den Benutzer ausgeben.

Ein Vector Store ist eine mögliche Retrieval-Komponente. RAG kann auch ohne Vektorsuche umgesetzt werden.

## Zentrale Begriffe

| Begriff | Aufgabe |
|---|---|
| Chunking | Dokumente in suchbare, fachlich sinnvolle Einheiten zerlegen |
| Embedding | Inhalt als numerischen Vektor für Ähnlichkeitssuche darstellen |
| Retrieval | Relevante Inhalte für eine konkrete Frage abrufen |
| Hybrid Search | Stichwortsuche und Vektorsuche kombinieren |
| Reranking | Gefundene Kandidaten mit einem weiteren Verfahren neu ordnen |
| Context Assembly | Geeignete Treffer innerhalb des Kontextbudgets zusammenstellen |
| Grounding | Die Antwort anhand der bereitgestellten Quellen begründen |

## Warum Chunking wichtig ist

Zu kleine Chunks verlieren fachlichen Zusammenhang; zu große Chunks enthalten viel irrelevanten Text. Überschriften, Tabellen und zusammengehörige Absätze sollten möglichst erhalten bleiben. Überlappungen können Zusammenhang bewahren, erzeugen aber auch redundante Treffer.

Es gibt keine universell richtige Chunk-Größe. Sie hängt von Dokumentstruktur, Fragen, Suchverfahren und Kontextbudget ab und sollte anhand repräsentativer Aufgaben bewertet werden.

## Welche Informationen das Modell erhält

Eine vereinfachte Prompt-Struktur kann so aussehen:

```text
Aufgabe:
Beantworte die Frage anhand der Quellen. Wenn diese nicht ausreichen,
benenne die fehlende Information. Gib die verwendeten Quellenkennungen an.

Quelle HR-2026, Abschnitt 4:
Bei einem privaten Umzug wird ein Arbeitstag Sonderurlaub gewährt.

Frage:
Wie viele Tage Sonderurlaub bekomme ich bei einem Umzug?
```

Quellenkennungen müssen aus den tatsächlich abgerufenen Dokumenten stammen. Eine zitierende Antwort sollte zusätzlich auf die korrekte Zuordnung zwischen Aussagen und Quellen geprüft werden.

## Grenzen und typische Fehler

| Problem | Konsequenz |
|---|---|
| Die richtige Information fehlt im Bestand | Auch gutes Retrieval kann sie nicht finden |
| Die Suche liefert unpassende Treffer | Dem Modell fehlt die passende Grundlage |
| Alte und neue Richtlinien werden vermischt | Die Antwort kann eine ungültige Regel verwenden |
| Zu viel Kontext wird eingefügt | Kosten und Laufzeit steigen; wichtige Stellen können untergehen |
| Das Modell ergänzt unbelegte Aussagen | Eine plausible Antwort ist nicht automatisch quellentreu |
| Berechtigungen werden erst nach der Generierung geprüft | Nicht freigegebene Inhalte können bereits beim Modell angekommen sein |

Zugriffsregeln gehören in den Abrufprozess, bevor Inhalte an das Modell übergeben werden. Inhalte aus Dokumenten sind Daten und sollten keine Anwendungsanweisungen überschreiben dürfen.

RAG aktualisiert die Modellgewichte nicht. Der zusätzliche Wissensbestand wird zur Laufzeit bereitgestellt. Änderungen am Dokumentbestand erfordern eine entsprechende Aktualisierung des Index.

## RAG und Fine-Tuning

| Ansatz | Typischer Schwerpunkt |
|---|---|
| RAG | Externe, aktualisierbare Informationen zur Laufzeit bereitstellen |
| Fine-Tuning | Modellverhalten durch zusätzliches Training verändern, etwa Aufgabenbearbeitung oder Ausgabeform |

Beide Ansätze können kombiniert werden. Die Wahl hängt davon ab, ob fehlende Informationen oder ungeeignetes Modellverhalten das eigentliche Problem sind.

## Qualität beurteilen

Retrieval und Generierung sollten getrennt geprüft werden: Wurden die benötigten Stellen gefunden? Beantwortet die Ausgabe die Frage? Sind alle wesentlichen Aussagen durch Quellen gedeckt? Wie verhalten sich Laufzeit und Kosten?

Ein sinnvoller Testbestand enthält auch Fragen ohne passende Quelle, widersprüchliche Dokumente und unterschiedliche Benutzerberechtigungen.

## Quellen

- [Lewis et al.: Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks](https://arxiv.org/abs/2005.11401)
- [Gao et al.: Retrieval-Augmented Generation for Large Language Models: A Survey](https://arxiv.org/abs/2312.10997)
- [Weaviate: Suchverfahren und Retrieval](https://docs.weaviate.io/weaviate/search)
