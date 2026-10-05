# Ollama mit Snap verwalten und Laufzeit-Logs verstehen

Diese Befehle gelten für eine Ollama-Installation über Snap unter Linux. Sie werden im Terminal ausgeführt.

## Dienste und Status anzeigen

```bash
snap services ollama
```

Die Ausgabe zeigt den genauen Dienstnamen sowie den Start- und Laufstatus. Bei mehreren Diensten kannst du statt `ollama` gezielt den angezeigten Namen im Format `ollama.<dienst>` verwenden.

## Letzte 100 Log-Zeilen anzeigen

```bash
sudo snap logs ollama -n=100
```

## Logs live verfolgen

```bash
sudo snap logs ollama -f
```

Mit `Strg+C` beendest du nur die Log-Anzeige. Ollama läuft weiter.

## Ollama neu starten

```bash
sudo snap restart ollama
```

Anschließend den Status prüfen:

```bash
snap services ollama
```

## Ollama stoppen und starten

```bash
sudo snap stop ollama
```

```bash
sudo snap start ollama
```

## API direkt testen

Die folgende Anfrage prüft direkt den Ollama-Server auf Port 11434 und umgeht den HTTP-Proxy:

```bash
curl --max-time 10 http://localhost:11434/api/tags
```

Eine JSON-Antwort mit der Modellliste zeigt, dass die API erreichbar ist. Sie bestätigt noch nicht, dass die Textgenerierung funktioniert.

## Textgenerierung mit llama3.2 testen

```bash
curl --max-time 120 http://localhost:11434/api/chat \
  -H 'Content-Type: application/json' \
  -d '{
    "model": "llama3.2",
    "messages": [
      {"role": "user", "content": "Hauptstadt von Deutschland"}
    ],
    "stream": true
  }'
```

Das Modell `llama3.2` muss bereits vorhanden sein. Verfolge während der Anfrage in einem zweiten Terminal die Logs:

```bash
sudo snap logs ollama -f
```

Wenn die direkte Anfrage funktioniert, die Spring-AI-Anfrage über den Proxy aber hängt, prüfe die Proxy-Verbindung und die konfigurierte Ollama-URL.

## Wenn kein Snap-Dienst gefunden wird

Wenn `snap services ollama` meldet, dass keine Dienste vorhanden sind, kann die Snap-Anwendung als gewöhnlicher Prozess gestartet worden sein. Die Befehle `snap logs` und `snap restart` gelten für Snap-Dienste. Prüfe zunächst die Ausgabe von:

```bash
snap list ollama
snap services ollama
```

## Token-Generierung: n_gen, tg und tg_3s

Diese Fortschrittsmeldungen stammen aus der Modell-Laufzeit, beispielsweise llama.cpp. Die genaue Darstellung hängt von Version und Backend ab.

| Feld | Bedeutung |
|---|---|
| `n_gen` | Anzahl der bisher für die aktuelle Anfrage generierten Tokens |
| `tg` | Durchschnittliche Generierungsgeschwindigkeit seit Beginn der Ausgabe, in Tokens pro Sekunde |
| `tg_3s` | Kurzfristige Generierungsgeschwindigkeit im jüngsten Messfenster von ungefähr drei Sekunden |
| `t/s` | Einheit: Tokens pro Sekunde |

Beispiel:

```text
n_gen = 300, tg = 25.0 t/s, tg_3s = 20.0 t/s
```

Bisher wurden 300 Tokens generiert, durchschnittlich 25 pro Sekunde. Im jüngsten Messfenster waren es ungefähr 20 pro Sekunde. Die Generierung ist damit zuletzt langsamer geworden.

Ein Token ist nicht unbedingt ein Wort: Es kann auch ein Wortteil oder Satzzeichen sein. Diese Angaben zeigen Fortschritt und Geschwindigkeit, nicht den erzeugten Text.

Wenn `n_gen` bei derselben Anfrage weiter steigt, arbeitet das Modell noch. Bei `"stream": false` erhält der HTTP-Client die Antwort erst nach Abschluss. Auch Spring AIs synchroner Aufruf mit `.call().content()` liefert dem Aufrufer erst die vollständige Antwort.

## Prompt-Verarbeitung: operator(), Kontext und Cache

`operator()` ist der Name einer internen C++-Funktion, aus der die Log-Meldung stammt. Er ist für sich genommen keine Fehlermeldung.

| Angabe | Bedeutung |
|---|---|
| `n_ctx_slot` | Maximale Kontextgröße für den Verarbeitungsslot in Tokens; Kapazität, nicht aktuelle Belegung |
| `task.n_tokens` | Token-Anzahl des eingehenden Prompts, einschließlich übergebener Chat-Historie und Chat-Template |
| `cached n_tokens` | Anzahl bereits berechneter Tokens, deren Kontextdaten für den neuen Prompt wiederverwendet werden |
| `n_keep` | Anzahl der Anfangstokens, die bei einer Kontextverschiebung erhalten bleiben sollen |
| `id` | Kennung des Verarbeitungsslots |
| `task` | Kennung der Anfrage |
| `memory_seq_rm [X, end)` | Entfernen der Cache-Einträge ab Position X bis zum Ende der betreffenden Sequenz |

Ein Slot ist ein interner Verarbeitungskontext. Seine Kennung ist nicht die `conversationId` aus der Spring-AI-Anwendung.

Beispiel:

```text
new prompt, n_ctx_slot = 4096, n_keep = 0, task.n_tokens = 850
cached n_tokens = 700, memory_seq_rm [700, end)
```

Der Slot besitzt eine Kontextkapazität von 4096 Tokens. Der eingehende Prompt umfasst 850 Tokens. Für 700 Tokens können vorhandene Berechnungsergebnisse wiederverwendet werden; die Cache-Einträge ab Position 700 werden entfernt. Der weitere Prompt wird anschließend verarbeitet. Je nach Laufzeit kann auch ein Token an der Übergangsstelle erneut berechnet werden.

Der Kontext enthält Prompt und generierte Fortsetzung. Ein größerer Prompt lässt entsprechend weniger Platz für die Fortsetzung. Was bei Erreichen der Kontextgrenze passiert, hängt von Modell, Laufzeit und Konfiguration ab.

`cached n_tokens = 0` ist kein Fehler. Es bedeutet, dass für diesen Aufruf keine passenden Kontextdaten wiederverwendet werden. Der Prompt wird neu verarbeitet.

## KV-Cache und Cassandra-ChatMemory unterscheiden

In Beispiel `08-ChatMemory` sendet Spring AI den gespeicherten Gesprächsverlauf erneut an den Modellserver. Bei passendem Kontext kann dieser bereits berechnete Teile über seinen KV-Cache wiederverwenden.

| Speicher | Inhalt und Zweck |
|---|---|
| Cassandra-ChatMemory | Nachrichtentexte und Rollen; erhält den Gesprächsverlauf über Anwendungsneustarts |
| KV-Cache der Modell-Laufzeit | Interne Key-/Value-Berechnungsergebnisse; beschleunigt die Verarbeitung passenden Kontexts |

Der KV-Cache trainiert das Modell nicht und ersetzt keine persistente Speicherung der Nachrichten. Nach einem Neustart der Modell-Laufzeit muss der erneut gesendete Verlauf gegebenenfalls vollständig neu verarbeitet werden.

## Hinweise bei scheinbar ausbleibenden Antworten

- Steigt `n_gen` für dieselbe Anfrage, läuft die Generierung weiter.
- Ein sinkendes `tg_3s` zeigt, dass die Generierung zuletzt langsamer geworden ist; die Ursache ist aus diesem Wert allein nicht erkennbar.
- Während der Prompt-Verarbeitung gibt es noch keine generierte Antwort. Ein großer Verlauf kann diese Phase verlängern.
- Ein Cache-Miss (`cached n_tokens = 0`) kann zusätzliche Prompt-Verarbeitung verursachen.
- Prüfe die API direkt auf Port 11434 und vergleiche mit dem Aufruf über den Proxy.

## Weiterführende Quellen

- [llama.cpp: Server-Implementierung](https://github.com/ggml-org/llama.cpp/blob/master/tools/server/server-context.cpp)
- [llama.cpp: Tutorial zur KV-Cache-Wiederverwendung](https://github.com/ggml-org/llama.cpp/discussions/13606)
