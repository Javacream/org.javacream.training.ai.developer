# Cassandra-Datensätze betrachten – 08-ChatMemory

Die gespeicherten Chat-Nachrichten lassen sich mit `cqlsh` direkt im Cassandra-Container betrachten. Eine zusätzliche Installation ist nicht erforderlich.

## Cassandra-Konsole öffnen

Im Projektverzeichnis `08-ChatMemory` ausführen:

```bash
docker compose exec cassandra cqlsh
```

Die folgenden CQL-Befehle werden in dieser Konsole eingegeben.

## Tabellenstruktur anzeigen

```sql
DESCRIBE TABLE spring_ai.chat_memory;
```

| Spalte | Bedeutung |
|---|---|
| `conversation_id` | Kennung des Gesprächs |
| `position` | Nachrichtenposition, beginnend bei 0 |
| `role` | Nachrichtenrolle, beispielsweise `USER` oder `ASSISTANT` |
| `content` | Text der Nachricht |

Der Primärschlüssel besteht aus `conversation_id` und `position`. Die Nachrichten eines Gesprächs sind nach ihrer Position geordnet.

## Alle gespeicherten Nachrichten anzeigen

```sql
SELECT conversation_id, position, role, content
FROM spring_ai.chat_memory;
```

## Ein bestimmtes Gespräch anzeigen

Der `PersistentMemoryController` verwendet standardmäßig die Gesprächskennung `persistent-demo`:

```sql
SELECT position, role, content
FROM spring_ai.chat_memory
WHERE conversation_id = 'persistent-demo'
ORDER BY position ASC;
```

Wenn du beim REST-Aufruf eine andere `conversationId` übergeben hast, ersetze `persistent-demo` durch diese Kennung.

## Lange Nachrichten übersichtlich darstellen

Mit `EXPAND ON` zeigt `cqlsh` die Datensätze vertikal an:

```sql
EXPAND ON;

SELECT *
FROM spring_ai.chat_memory
WHERE conversation_id = 'persistent-demo';
```

Zur normalen Tabellenansicht zurückkehren:

```sql
EXPAND OFF;
```

Die Konsole verlassen:

```sql
EXIT;
```

## Abfrage direkt aus dem Linux-Terminal ausführen

Ohne eine interaktive Cassandra-Konsole zu öffnen:

```bash
docker compose exec cassandra cqlsh -e \
  "SELECT * FROM spring_ai.chat_memory WHERE conversation_id = 'persistent-demo';"
```

## Welche Nachrichten werden gespeichert?

In Beispiel 08 speichert nur der `PersistentMemoryController` seine Nachrichten in Cassandra. Die anderen Memory-Controller verwenden Speicher im Java-Prozess.

Der persistente Controller hält maximal 20 Nachrichten. Das Repository ersetzt bei jedem Speichern den bisherigen Gesprächsverlauf durch die aktuelle Nachrichtenliste. Die Tabelle enthält deshalb den aktuellen Verlauf und kein vollständiges historisches Protokoll aller Nachrichten.

Die Tabelle wird beim Start der Anwendung durch `TrainingCassandraChatMemoryRepository` erstellt. Wenn sie noch nicht existiert, starte die Anwendung mit erreichbarer Cassandra-Datenbank. Wenn die Tabelle leer ist, rufe zunächst den Endpunkt `/api/persistent-memory` auf und führe danach die Abfrage erneut aus.
