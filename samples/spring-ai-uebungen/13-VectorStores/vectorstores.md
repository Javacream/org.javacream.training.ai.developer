# Vector Stores – Speicherung und Suche mit Embeddings

Ein Vector Store speichert numerische Vektoren und ermöglicht die Suche nach ähnlichen Vektoren. In KI-Anwendungen repräsentieren diese Vektoren häufig Texte, Bilder oder andere Inhalte. Die Vektoren werden durch ein Embedding-Modell erzeugt.

Für Text bedeutet das: Statt nur nach identischen Wörtern zu suchen, kann eine Anwendung Inhalte mit ähnlicher Bedeutung finden. Ob die Suchergebnisse fachlich geeignet sind, hängt vom Embedding-Modell, den Daten und der Suchkonfiguration ab.

## Was ist ein Embedding?

Ein Embedding ist eine Zahlenliste mit einer festen Dimension. Eine stark vereinfachte Darstellung könnte so aussehen:

```text
"Passwort vergessen"       → [0.21, -0.34, 0.88, ...]
"Zugangsdaten zurücksetzen" → [0.19, -0.31, 0.85, ...]
"Urlaubsantrag stellen"    → [-0.72, 0.56, 0.03, ...]
```

Die Zahlen sind illustrative Beispiele. Einzelne Koordinaten sind normalerweise keine direkt benennbaren Fachmerkmale. Entscheidend ist die Lage der Vektoren zueinander im vom Modell erzeugten Vektorraum.

## Was gespeichert wird

| Bestandteil | Beispiel | Zweck |
|---|---|---|
| ID | `doc-4711-chunk-3` | Eindeutige Identifikation, Aktualisierung und Löschung |
| Vektor | Liste von Fließkommazahlen | Ähnlichkeitssuche |
| Inhalt oder Inhaltsreferenz | Abschnitt einer IT-Anleitung oder Verweis auf dessen Ablage | Rückgabe der gefundenen Information |
| Metadaten | Quelle, Kategorie, Datum, Sprache, Mandant, Version | Filterung und Nachvollziehbarkeit |

Je nach Produkt liegen Originalinhalte direkt im Store oder werden über eine Referenz aus einem anderen Speicher geladen.

## Daten aufnehmen

1. Inhalt bereitstellen und gegebenenfalls in Abschnitte zerlegen.
2. Mit einem Embedding-Modell den Vektor berechnen.
3. Vektor, ID und Metadaten speichern.
4. Den Suchindex aktualisieren.

Manche Produkte berechnen Embeddings über eine integrierte Modellanbindung. Andere erwarten fertige Vektoren von der Anwendung.

## Eine Suchanfrage ausführen

1. Die Frage mit einem zum Datenbestand passenden Embedding-Modell in einen Vektor umwandeln.
2. Nach den ähnlichsten gespeicherten Vektoren suchen.
3. Metadatenbedingungen und gegebenenfalls Distanzgrenzen anwenden.
4. Die passenden Datensätze mit Inhalt und Suchwerten zurückgeben.

Die Vektorsuche erzeugt dabei noch keine sprachliche Antwort. In einer RAG-Anwendung folgt die Antwortgenerierung erst nach dem Retrieval.

## Ähnlichkeitsmaße

| Maß | Grundidee |
|---|---|
| Cosine Similarity | Vergleicht die Richtung zweier Vektoren |
| Dot Product | Skalarprodukt; berücksichtigt abhängig von der Normalisierung auch die Vektorlängen |
| Euclidean Distance | Räumlicher Abstand zwischen zwei Vektoren |

Das Maß muss zum Embedding-Modell und zur Indexkonfiguration passen. Bei normalisierten Vektoren bestehen mathematische Beziehungen zwischen diesen Maßen. Ohne passende Voraussetzungen sind sie nicht beliebig austauschbar.

Ein Suchwert von 0,8 bedeutet nicht automatisch „80 Prozent relevant“. Produkte liefern unterschiedliche Distanzen, Ähnlichkeiten oder transformierte Scores. Bei Distanzen ist häufig kleiner besser, bei Ähnlichkeiten größer; die konkrete Definition steht in der Produktdokumentation.

## Exakte und approximative Suche

Eine exakte Suche vergleicht den Anfragevektor mit allen relevanten gespeicherten Vektoren. Bei großen Beständen kann das teuer sein.

Approximate Nearest Neighbor (ANN) verwendet Indexstrukturen, um gute Nachbarn schneller zu finden. Ein verbreitetes Verfahren ist HNSW, ein Graph mit mehreren Ebenen. Die Suche besucht eine Auswahl aussichtsreicher Kandidaten statt den gesamten Bestand.

Diese Beschleunigung ist ein Kompromiss: Die Ergebnisse müssen nicht exakt den mathematisch nächsten Nachbarn entsprechen. Index- und Suchparameter beeinflussen Speicherbedarf, Laufzeit und Retrieval-Recall.

## topK, Schwellenwerte und Metadatenfilter

| Parameter | Wirkung |
|---|---|
| `topK` | Begrenzt die Anzahl zurückzugebender Treffer |
| Ähnlichkeits- oder Distanzschwelle | Entfernt Treffer, deren Suchwert die Bedingung nicht erfüllt |
| Metadatenfilter | Beschränkt die Suche anhand konkreter Eigenschaften |

Beispiel als Pseudocode, keine standardisierte Query Language:

```text
search(
  vector = embed("Wie setze ich mein Passwort zurück?"),
  topK = 5,
  filter = language == "de" AND category == "IT"
)
```

`topK = 5` macht schlechte Treffer nicht relevant und garantiert nach Filterung keine fünf Ergebnisse. Ob Filter vor, während oder nach der ANN-Suche berücksichtigt werden, hängt vom Produkt und dessen Konfiguration ab.

## Hybrid Search

Vektorsuche eignet sich für sinngleiche Formulierungen. Stichwortsuche ist oft hilfreich bei Produktnummern, Namen und Fachbegriffen, die exakt vorkommen sollen.

Hybrid Search kombiniert beide Verfahren, beispielsweise Vektorsuche und BM25. Die Trefferrangfolgen oder Scores werden zusammengeführt. Ein anschließendes Reranking kann die Auswahl weiter verbessern.

## Vector Store und Vector Database

Vector Store ist ein allgemeiner Begriff für eine Komponente, die Vektoren speichert und durchsucht. Das kann eine Bibliothek, ein Suchsystem oder eine Datenbank sein. Eine Vector Database bietet darüber hinaus Datenbankfunktionen wie Persistenz, Updates, Filterung und Betriebsfunktionen; der Umfang ist produktabhängig.

Beispiele für unterschiedliche Ansätze sind spezialisierte Systeme wie Qdrant, Milvus und Weaviate, PostgreSQL mit pgvector sowie Suchsysteme wie Elasticsearch oder OpenSearch. Sie haben unterschiedliche Schnittstellen und Betriebseigenschaften. Es gibt keine einheitliche, produktübergreifende Vector-Store-Query-Language.

## Wichtige Grenzen

- Dokument- und Anfragevektoren müssen im selben kompatiblen Vektorraum liegen. Gleiche Dimensionen allein reichen nicht aus.
- Bei einem Wechsel des Embedding-Modells müssen die betroffenen Daten normalerweise neu eingebettet und indexiert werden.
- Neue Inhalte, Änderungen und Löschungen müssen in Inhaltsspeicher und Index konsistent verarbeitet werden.
- Ähnliche Inhalte können fachlich falsch oder veraltet sein. Ähnlichkeit ist kein Wahrheitsnachweis.
- Mandanten- und Zugriffsgrenzen müssen vor der Weitergabe gefundener Inhalte wirksam durchgesetzt werden.

## Verbindung zu RAG und Evaluation

| Konzept | Aufgabe |
|---|---|
| Vector Store | Speichert Embeddings und liefert ähnliche Inhalte |
| RAG | Verwendet abgerufene Inhalte zur Antwortgenerierung |
| Evaluation | Prüft Qualität und Verhalten von Suche und Antwort |

Ein Vector Store kann auch ohne Sprachmodell eingesetzt werden, beispielsweise für ähnliche Produkte oder Dokumente. Für seine Bewertung sind geeignete Testfragen, bekannte relevante Treffer, Suchlaufzeit und Indexgröße wichtig.

## Quellen

- [Weaviate: Vektorsuche](https://docs.weaviate.io/weaviate/search/similarity)
- [Weaviate: Hybrid Search](https://docs.weaviate.io/weaviate/concepts/search/hybrid-search)
- [pgvector: Vektorspeicherung, Distanzen und Indexverfahren](https://github.com/pgvector/pgvector)
- [Qdrant-Dokumentation](https://qdrant.tech/documentation/)
