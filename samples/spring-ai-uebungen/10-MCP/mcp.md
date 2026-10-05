# MCP – Model Context Protocol

MCP ist ein offenes Protokoll, mit dem KI-Anwendungen auf externe Daten und Funktionen zugreifen können. Ein MCP-Server stellt diese Fähigkeiten in einem einheitlichen Format bereit, sodass verschiedene KI-Anwendungen dieselbe Integration verwenden können.

Angenommen, ein Assistent soll beantworten: „Welche offenen Bestellungen hat Kunde 4711?“ Das Sprachmodell kennt deine aktuellen Bestellungen nicht. Eine Anwendung muss sie aus dem entsprechenden System holen und dem Modell zur Verfügung stellen. MCP standardisiert die Kommunikation mit der Komponente, die diesen Zugriff anbietet.

## Die beteiligten Komponenten

MCP unterscheidet drei Rollen:

| Rolle | Aufgabe |
|---|---|
| **Host** | Die KI-Anwendung, beispielsweise ein Chatprogramm oder eine Entwicklungsumgebung. Sie verwaltet Modell, Benutzerinteraktion und Zugriffsentscheidungen. |
| **Client** | Die MCP-Komponente innerhalb des Hosts. Sie stellt die Verbindung zu einem MCP-Server her. |
| **Server** | Stellt Daten, ausführbare Funktionen und Prompt-Vorlagen bereit. Er kann beispielsweise eine Datenbank oder eine bestehende REST-API anbinden. |

Ein Host kann mehrere Clients und damit mehrere Server anbinden, etwa für Bestellungen, Dokumente und ein Git-Repository. Das Sprachmodell muss MCP dabei nicht selbst implementieren: Die Anwendung vermittelt zwischen Modell und MCP-Client.

## Was ein Server bereitstellen kann

Die drei zentralen Angebote sind **Tools, Resources und Prompts**:

| Angebot | Bedeutung | Beispiel |
|---|---|---|
| **Tools** | Ausführbare Funktionen, die Informationen abrufen oder Aktionen ausführen | `find_orders(customerId)` oder `create_ticket(title)` |
| **Resources** | Inhalte, die die Anwendung als Kontext verwenden kann | Eine Datei, eine Produktbeschreibung oder ein Dokument |
| **Prompts** | Wiederverwendbare Vorlagen für Modellanfragen | „Prüfe diesen Code anhand unserer Qualitätsregeln“ |

Konzeptionell wählt das Modell passende Tools, die Anwendung verwaltet Resources und der Benutzer wählt Prompts. Der Host entscheidet weiterhin, welche Fähigkeiten tatsächlich verfügbar sind und welche Aufrufe erlaubt werden.

Ein Tool wird mit Namen, Beschreibung und einem Schema für seine Argumente angeboten. Dadurch kann die Anwendung dem Modell erklären, welche Funktion verfügbar ist und welche Eingaben sie erwartet.

## Wie eine Anfrage abläuft

Beim Bestellungsbeispiel sieht der Ablauf so aus:

1. Die KI-Anwendung verbindet sich mit dem MCP-Server. Beide Seiten handeln Protokollversion und unterstützte Fähigkeiten aus.
2. Der Client fragt die verfügbaren Tools ab, beispielsweise mit `tools/list`.
3. Die Anwendung macht dem Modell die passenden Tool-Beschreibungen zugänglich.
4. Auf die Benutzerfrage hin fordert das Modell beispielsweise `find_orders` mit `customerId = 4711` an.
5. Nach den Zugriffsentscheidungen des Hosts ruft der Client das Tool über `tools/call` auf.
6. Der Server fragt das Bestellsystem ab und liefert das Ergebnis zurück.
7. Die Anwendung gibt das Ergebnis an das Modell weiter, das daraus die Antwort formuliert.

Die eigentliche Datenbankabfrage implementiert der Server. MCP standardisiert das Entdecken und Aufrufen der Funktion sowie den Austausch ihrer Ergebnisse.

Resources und Prompts haben entsprechende Protokolloperationen: beispielsweise `resources/read` zum Lesen einer Ressource und `prompts/get` zum Abrufen einer ausgefüllten Prompt-Vorlage. Eine abgerufene Vorlage ist noch keine Modellantwort; die Anwendung muss ihre Nachrichten anschließend an das Modell übergeben.

## Wie die Kommunikation technisch funktioniert

MCP verwendet **JSON-RPC** für strukturierte Anfragen, Antworten und Benachrichtigungen. Übliche Transportwege sind:

| Transport | Typischer Einsatz |
|---|---|
| **stdio** | Der Host startet einen lokalen Serverprozess und kommuniziert über dessen Standardeingabe und Standardausgabe. |
| **Streamable HTTP** | Der Client verbindet sich über HTTP mit einem lokal oder entfernt laufenden Server. |

Ein MCP-Server braucht daher nicht zwingend einen Netzwerkport. Ein lokal gestarteter Prozess kann bereits ein vollständiger MCP-Server sein.

## Was MCP für die Architektur bringt

Ohne ein gemeinsames Protokoll braucht jede KI-Anwendung eigene Integrationen für die jeweiligen Systeme. Mit MCP kann ein System seine Fähigkeiten einmal über einen MCP-Server anbieten; kompatible Hosts können diese Schnittstelle wiederverwenden.

Dabei bleibt die fachliche Implementierung erhalten: Ein MCP-Server kann intern REST, SQL, Dateizugriffe oder andere APIs verwenden. Seine öffentliche MCP-Schnittstelle beschreibt, was die KI-Anwendung entdecken und nutzen kann.

Eine Verbindung allein sorgt noch nicht dafür, dass ein Tool aufgerufen wird. Die Anwendung muss es zugänglich machen, und die Modellsteuerung muss einen passenden Aufruf veranlassen. Ebenso erledigt MCP keine automatische Suche über sämtliche Dokumente: Welche Resources geladen oder welche Suchtools ausgeführt werden, bestimmt die jeweilige Anwendung.

## Weiterführende Quellen

- [Offizielle MCP-Dokumentation und Spezifikation](https://modelcontextprotocol.io/)
- [MCP Python SDK: Client und Transportwege](https://py.sdk.modelcontextprotocol.io/client/)
- [MCP TypeScript SDK: Tools, Resources und Prompts aufrufen](https://ts.sdk.modelcontextprotocol.io/v2/clients/calling)
- [MCP TypeScript SDK: Server und Tool-Schemata](https://ts.sdk.modelcontextprotocol.io/server)
