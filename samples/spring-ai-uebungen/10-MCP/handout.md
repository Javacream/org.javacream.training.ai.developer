# Handout: MCP

*Projekt 10-MCP*

Dieses Handout erläutert die 7 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8090. Swagger UI: http://localhost:8090/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

**MCP-Profile **Im Basisprofil sind MCP-Client und MCP-Server deaktiviert. Starte den Server zuerst mit mcp-server auf Port 8090 und den Client separat mit mcp-client auf Port 8190. Beide Instanzen verwenden dasselbe Projekt.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mcp-server
# In einem zweiten Terminal:
mvn spring-boot:run -Dspring-boot.run.profiles=mcp-client
```

## Überblick über die Lernfolge

McpAnnotationsController – MCP-Fähigkeiten über Annotationen beschreiben.

McpClientController – Verfügbare MCP-Werkzeuge über das Protokoll entdecken.

McpPromptsController – Einen serverseitigen Prompt abrufen.

McpResourcesController – Eine MCP-Ressource lesen.

McpServerResourcesController – Die Resource-Implementierung lokal untersuchen.

McpServerToolsController – Eine Tool-Implementierung unabhängig vom Modell prüfen.

McpToolsController – MCP-Tools an den ChatClient anbinden.

## 1 McpAnnotationsController

POST /api/mcp-annotations

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **MCP-Fähigkeiten über Annotationen beschreiben.

**Didaktischer Sinn **TrainingMcpFeatures definiert Tool, Resource und Prompt. Dieser REST-Endpunkt ruft explain direkt auf und macht das erzeugte PromptResult sichtbar.

```java
return features.explain(message);
```

**Gezielte Übung **Sende „Dependency Injection“ und untersuche die zurückgegebene User Message.

**Worauf es ankommt **Dieser REST-Aufruf durchläuft keinen MCP-Transport und keinen Modellaufruf. Die Veröffentlichung per MCP erfolgt über Scanner und Server-Profil.

## 2 McpClientController

POST /api/mcp-client

Eingabe: Kein Request-Body erforderlich.

**Lernziel **Verfügbare MCP-Werkzeuge über das Protokoll entdecken.

**Didaktischer Sinn **Der synchrone Client ruft listTools auf. Das trennt Discovery von der späteren Auswahl und Ausführung eines Tools.

```java
return client().listTools();
```

**Gezielte Übung **Starte Server und Client mit den vorgesehenen Profilen. Prüfe, ob add mit Beschreibung und Argumenten angeboten wird.

**Worauf es ankommt **Ohne Client-Profil liefert die fehlende Verbindung HTTP 503. Werkzeuge aufzulisten führt sie nicht aus.

## 3 McpPromptsController

POST /api/mcp-prompts

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **Einen serverseitigen Prompt abrufen.

**Didaktischer Sinn **getPrompt fordert explain mit dem Argument topic an. So wird eine Prompt-Vorlage als eigene MCP-Fähigkeit sichtbar.

```java
return client().getPrompt(new
McpSchema.GetPromptRequest("explain",Map.of("topic",message)));
```

**Gezielte Übung **Rufe den Client mit zwei unterschiedlichen Themen auf. Vergleiche die erzeugten Prompt-Messages.

**Worauf es ankommt **Ein abgerufener MCP-Prompt ist noch keine Modellantwort. Der Controller sendet ihn nicht anschließend an Ollama.

## 4 McpResourcesController

POST /api/mcp-resources

Eingabe: Kein Request-Body erforderlich.

**Lernziel **Eine MCP-Ressource lesen.

**Didaktischer Sinn **readResource fordert training://guide vom Server an. Das stellt Resource-Zugriff neben Tools und Prompts als eigenes Konzept.

```java
return client().readResource(new McpSchema.ReadResourceRequest("training://guide"));
```

**Gezielte Übung **Lies die Ressource über den Client und vergleiche sie mit dem direkten Server-REST-Endpunkt.

**Worauf es ankommt **Die URI identifiziert eine MCP-Ressource, keine gewöhnliche Webadresse. Lesen erzeugt hier keinen Chat-Aufruf.

## 5 McpServerResourcesController

POST /api/mcp-server-resources

Eingabe: Kein Request-Body erforderlich.

**Lernziel **Die Resource-Implementierung lokal untersuchen.

**Didaktischer Sinn **features.guide() gibt den festen Trainingshinweis zurück. Die Übung macht den Code hinter der MCP-Veröffentlichung zugänglich.

```java
return features.guide();
```

**Gezielte Übung **Vergleiche den lokalen REST-Wert mit dem Resource-Ergebnis des MCP-Clients.

**Worauf es ankommt **Dieser Controller ruft die Java-Methode direkt auf. Transport, MCP-Hülle und Discovery werden dabei umgangen.

## 6 McpServerToolsController

POST /api/mcp-server-tools

Eingabe: Query-Parameter gemäß Swagger.

**Lernziel **Eine Tool-Implementierung unabhängig vom Modell prüfen.

**Didaktischer Sinn **features.add(a,b) summiert zwei Integer. Der direkte Aufruf isoliert die Java-Funktion von MCP und Modellentscheidung.

```java
return features.add(a,b);
```

**Gezielte Übung **Rufe mit a=2 und b=3 auf. Vergleiche mit einem modellgesteuerten MCP-Tool-Aufruf.

**Worauf es ankommt **Der REST-Endpunkt ist kein MCP-Endpoint. Parameter kommen hier vom HTTP-Client; Math.addExact prüft Überläufe.

## 7 McpToolsController

POST /api/mcp-tools

Eingabe: Text im Body (Content-Type: text/plain).

**Lernziel **MCP-Tools an den ChatClient anbinden.

**Didaktischer Sinn **Ein ToolCallbackProvider stellt entfernte Werkzeuge bereit. Der ChatClient kann sie dem Modell anbieten, während die eigentliche Ausführung zum MCP-Server delegiert wird.

```java
var tools=provider.getIfAvailable();
if(tools==null) throw new
org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Profil
mcp-client benötigt");
return chatClient.prompt()
.user(message)
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Bitte das add-Tool für zwei Zahlen zu verwenden. Beobachte sowohl Ollama-Requests als auch den Server-Haltepunkt.

**Worauf es ankommt **Client-Profil und erreichbarer Server sind nötig. Die Modellentscheidung bleibt offen; entfernte Tools haben zusätzliche Transport- und Fehlergrenzen.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, MCP. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
