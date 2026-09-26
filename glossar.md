# Glossar – AI-Begriffe im Spring-AI-Seminar

Dieses Glossar fasst die zentralen AI- und LLM-Begriffe des Seminars zusammen. Die Blockangaben zeigen, in welchen Seminarblöcken der jeweilige Begriff eingeführt oder wesentlich verwendet wird.

| Begriff | Bedeutung | Block |
|---|---|---:|
| **Advisor** | Spring-AI-Konzept für wiederverwendbare Logik vor bzw. nach einem Modellaufruf, z. B. Memory, Logging oder RAG. | 1, 3, 5, 8 |
| **Advisor Chain** | Kette mehrerer Advisors, die einen AI-Request bzw. die Response nacheinander verarbeiten können. | 3 |
| **Agent / agentischer Workflow** | Ablauf, bei dem ein Modell auf Basis eines Ziels wiederholt Entscheidungen trifft, Tools verwendet und deren Ergebnisse für weitere Schritte nutzt. | 6, 7 |
| **ChatClient** | Komfortable Spring-AI-API für die Interaktion mit einem Chat-Modell. | 1–3, 5, 8 |
| **ChatModel** | Spring-AI-Abstraktion für ein Chat-/Sprachmodell. Der konkrete Provider, etwa Ollama, liegt dahinter. | 1, 2, 8 |
| **Chat Memory / Conversation Memory** | Von der Anwendung verwalteter Gesprächskontext, der bei späteren Modellaufrufen wieder bereitgestellt werden kann. | 3, 5, 8 |
| **Chunk** | Teilstück eines größeren Dokuments. Dokumente werden für RAG typischerweise in Chunks zerlegt und einzeln eingebettet. | 4, 5 |
| **Chunking** | Aufteilung größerer Dokumente in geeignete Textabschnitte für Embedding und Retrieval. | 4, 5 |
| **Completion** | Vom Modell erzeugte Fortsetzung eines vorhandenen Kontexts. Daraus stammt auch der Begriff Completion Tokens. | 1 |
| **Completion Tokens / Output Tokens** | Tokens, die das Modell als Antwort neu erzeugt. | 1, 8 |
| **Context / Kontext** | Gesamtheit der Informationen, die dem Modell für einen Aufruf zur Verfügung stehen, z. B. System Prompt, Benutzerfrage, Memory und RAG-Inhalte. | 1, 3–5, 7, 8 |
| **Context Window / Kontextfenster** | Maximale Menge an Tokens, die ein Modell innerhalb einer Verarbeitung berücksichtigen kann. | 1, 3, 4 |
| **Conversation ID** | Kennung, mit der Chat Memory unterschiedlichen Gesprächen bzw. Benutzern zugeordnet wird. | 3 |
| **Correctness** | Evaluationskriterium: Ist eine generierte Antwort fachlich korrekt? | 8 |
| **Embedding** | Numerische Vektorrepräsentation eines Inhalts, die dessen semantische Eigenschaften abbilden soll. | 1, 4, 5 |
| **Embedding Model** | Modell, das Texte in Embedding-Vektoren umwandelt. Im Seminar verwenden wir `nomic-embed-text`. | 1, 4, 5 |
| **Evaluation** | Systematische Bewertung der Qualität einer AI-Anwendung bzw. ihrer Antworten. | 5, 8 |
| **Evaluator** | Komponente bzw. Verfahren zur Bewertung einer generierten Antwort anhand definierter Kriterien. | 8 |
| **Faithfulness** | Maß dafür, ob Aussagen einer generierten Antwort durch den bereitgestellten Kontext gestützt werden. | 8 |
| **Generative AI / Generative KI** | AI-Systeme, die neue Inhalte wie Text erzeugen, statt lediglich vorhandene Daten zu klassifizieren oder auszuwerten. | 1 |
| **Groundedness / Grounding** | Bindung einer generierten Antwort an bereitgestellte, überprüfbare Informationen, insbesondere bei RAG. | 5, 8 |
| **Guardrail** | Technische oder organisatorische Begrenzung eines AI-Systems, z. B. Input-Prüfung, Tool-Allowlist oder Output-Validierung. | 7, 8 |
| **Halluzination** | Inhaltlich unbelegte oder falsche Ausgabe eines Modells, die dennoch plausibel formuliert sein kann. | 1, 5 |
| **Human-in-the-loop** | Verfahren, bei dem eine kritische AI-gestützte Entscheidung oder Aktion vor ihrer Ausführung von einem Menschen geprüft bzw. bestätigt wird. | 6, 7 |
| **Indirect Prompt Injection** | Manipulative Instruktion, die nicht direkt vom Benutzer kommt, sondern beispielsweise über ein von RAG geladenes Dokument in den Modellkontext gelangt. | 7 |
| **Inference / Inferenz** | Ausführung eines trainierten Modells zur Erzeugung einer Antwort. Bei Ollama findet die Inferenz lokal statt. | 1 |
| **Input Tokens** | Andere Bezeichnung für Prompt Tokens – die Tokens, die dem Modell als Eingabe übergeben werden. | 1 |
| **Large Language Model (LLM)** | Sprachmodell, das aus großen Trainingsdaten statistische Sprachmuster gelernt hat und daraus neue Textausgaben generiert. | 1 |
| **LLM-basierte Evaluation** | Verwendung eines Sprachmodells zur Bewertung der Ausgabe eines anderen Modellaufrufs. | 8 |
| **Message** | Einzelne Nachricht innerhalb eines Modellkontexts, beispielsweise System- oder User-Message. | 1–3 |
| **Metadata / Metadaten** | Zusatzinformationen zu Dokumenten oder Modellaufrufen. Bei RAG beispielsweise Quelle und Dokumenttyp, bei Modellaufrufen u. a. Token-Nutzung. | 1, 4, 5 |
| **MCP – Model Context Protocol** | Standardisiertes Protokoll, über das AI-Anwendungen externe Tools, Ressourcen und weitere Fähigkeiten nutzen können. | 6, 7, 8 |
| **MCP Client** | Komponente einer AI-Anwendung, die sich mit einem MCP Server verbindet und dessen Fähigkeiten nutzt. | 7 |
| **MCP Server** | Stellt Tools, Resources und andere Fähigkeiten über MCP standardisiert zur Verfügung. | 7 |
| **Model Provider** | Anbieter bzw. Laufzeitumgebung eines AI-Modells, beispielsweise Ollama oder ein Cloud-Anbieter. | 1, 2 |
| **Observability** | Beobachtbarkeit des AI-Systems anhand von Metriken und Traces, z. B. Latenz, Fehler und Token-Nutzung. | 8 |
| **Ollama** | Lokale Laufzeitumgebung für AI-Modelle. Im Seminar betreibt Ollama `llama3.2` und `nomic-embed-text`. | 1, 4, 8 |
| **Output Tokens** | Andere Bezeichnung für Completion Tokens – vom Modell neu erzeugte Tokens. | 1, 8 |
| **Output Validation** | Prüfung generierter Modellausgaben gegen technische oder fachliche Regeln vor ihrer Weiterverarbeitung. | 2, 7 |
| **Prompt** | Inhaltliche Eingabe bzw. Instruktion für einen Modellaufruf. Sie kann aus mehreren Nachrichten und zusätzlichen Kontextinformationen bestehen. Sie ist vom Begriff Prompt Tokens zu unterscheiden. | 1–8 |
| **Prompt Engineering** | Systematische Gestaltung von Prompts mit dem Ziel, geeignete und reproduzierbarere Modellergebnisse zu erhalten. | 2, 5 |
| **Prompt Injection** | Versuch, durch manipulierte Eingaben Regeln oder ursprüngliche Instruktionen einer AI-Anwendung zu umgehen. | 7, 8 |
| **Prompt Template** | Wiederverwendbare Prompt-Struktur mit variablen Bestandteilen. | 2 |
| **Prompt Tokens / Input Tokens** | Durch den Tokenizer erzeugte bzw. gezählte Tokens der Modelleingabe. Dazu können User Prompt, System Prompt, Memory, RAG-Kontext und weitere Informationen gehören. | 1, 3–5, 8 |
| **RAG – Retrieval Augmented Generation** | Verfahren, bei dem vor der Antwort relevante externe Informationen gesucht und dem LLM als zusätzlicher Kontext bereitgestellt werden. | 1, 3–5, 7, 8 |
| **Relevance** | Evaluationskriterium dafür, wie gut eine Antwort zur gestellten Frage passt. | 8 |
| **Resource** | Über MCP bereitgestellte Daten- bzw. Kontextquelle im Unterschied zu einer ausführbaren Funktion. | 7 |
| **Retrieval** | Suche nach für eine Anfrage relevanten Informationen, insbesondere in einem Vector Store. | 4, 5, 8 |
| **Similarity Search** | Suche nach semantisch ähnlichen Vektoren in einem Vector Store. | 4, 5 |
| **Similarity Threshold** | Mindestwert für die Ähnlichkeit, ab der ein Dokument als Retrieval-Treffer berücksichtigt wird. | 4, 5 |
| **Structured Output** | Modellausgabe in einer definierten Struktur, die beispielsweise auf einen Java-Datentyp abgebildet werden kann. | 2, 7 |
| **System Prompt / System Message** | Instruktion, die grundlegende Rolle, Verhalten oder Regeln des Modells für einen Aufruf festlegt. | 2, 5, 7 |
| **Temperature** | Modellparameter zur Beeinflussung der Variation bei der Auswahl erzeugter Tokens. | 1, 2 |
| **Token** | Verarbeitungseinheit eines Sprachmodells. Texte werden durch einen Tokenizer in Tokens zerlegt. | 1, 3, 8 |
| **Tokenizer** | Verfahren bzw. Komponente, die Text in die vom Modell verarbeiteten Tokens zerlegt. | 1 |
| **Tool** | Von der Anwendung bereitgestellte Funktion, die ein Modell im Rahmen eines Tool Calls anfordern kann. | 1, 6–8 |
| **Tool Calling** | Mechanismus, bei dem ein Modell entscheidet, eine von der Anwendung bereitgestellte Funktion aufzurufen. | 6–8 |
| **Tool Calling Loop** | Wiederholter Ablauf aus Modellentscheidung, Tool-Aufruf, Tool-Ergebnis und erneuter Modellentscheidung. | 6 |
| **Tool Misuse** | Ungeeignete, unerwartete oder unzulässige Verwendung eines bereitgestellten Tools durch einen AI-Workflow. | 6, 7 |
| **Top-K** | Anzahl der ähnlichsten Dokumente bzw. Chunks, die beim Retrieval berücksichtigt werden sollen. | 4, 5 |
| **Total Tokens** | Summe aus Prompt Tokens und Completion Tokens eines Modellaufrufs. | 1, 8 |
| **Vector / Vektor** | Zahlenfolge, mit der ein Embedding mathematisch repräsentiert wird. | 4 |
| **Vector Store** | Speicher- und Suchsystem für Embeddings, das insbesondere semantische Ähnlichkeitssuche ermöglicht. | 1, 4, 5, 8 |
