# Handout: ChatMemory

*Projekt 08-ChatMemory*

Dieses Handout erläutert die 6 Controller des Projekts mit Lernziel, didaktischem Sinn und gezielten Übungen. Die Beispiele werden am tatsächlichen Aufrufpfad untersucht: REST-Eingabe, Spring-AI-Verarbeitung und Ergebnis. Die folgenden Codeauszüge zeigen den Kern der jeweiligen execute-Methode.

## Vorbereitung und gemeinsame Konfiguration

Benötigt werden JDK 17 oder neuer, Maven 3.9+ und ein laufendes Ollama. Die Modellintegration verwendet normalerweise llama3.2 mit Temperature 0.2; Modelle werden nicht automatisch heruntergeladen. Weitere Voraussetzungen stehen in requirements.md des Projekts.

```bash
ollama pull llama3.2
mvn spring-boot:run
```

Die Anwendung läuft auf Port 8088. Swagger UI: http://localhost:8088/swagger-ui.html. Die Proxy-Weboberfläche läuft bei dir auf Port 9082. Für Ollama-Aufrufe über den Proxy:

```bash
OLLAMA_BASE_URL=http://localhost:11435 mvn spring-boot:run
```

**Datenbank **Cassandra 5 muss erreichbar sein. Die Projekt-Compose-Datei und Initialisierung sind unabhängig vom HTTP-Proxy. Mehrere Compose-Stacks mit demselben Host-Port 9042 nicht gleichzeitig starten. Für Cassandra mindestens 4 GB RAM vorsehen.

```bash
docker compose up -d --wait
docker compose run --rm cassandra-init
```

## Überblick über die Lernfolge

ChatMemoryController – Gesprächsverlauf manuell verwalten.

ConversationMemoryController – Gespräche durch eine ID getrennt halten.

InMemoryRepositoryController – Repository und ChatMemory unterscheiden.

MemoryAdvisorController – Die manuelle Memory-Logik durch einen Advisor ersetzen.

MessageWindowController – Die Größe des gespeicherten Gesprächsfensters verstehen.

PersistentMemoryController – Gesprächsverlauf über Anwendungsneustarts erhalten.

## 1 ChatMemoryController

POST /api/chat-memory

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Gesprächsverlauf manuell verwalten.

**Didaktischer Sinn **Der Controller speichert die User Message, übergibt die gespeicherten Nachrichten als Prompt und speichert anschließend die Assistant-Antwort. Das macht die Schritte hinter Memory-Advisors sichtbar.

```java
memory.add(conversationId,new UserMessage(message));
var response = chatClient.prompt(new
org.springframework.ai.chat.prompt.Prompt(memory.get(conversationId)))
.call().chatResponse();
memory.add(conversationId,response.getResult().getOutput());
return
Map.of("answer",response.getResult().getOutput().getText(),"messages",memory.get(conversationId));
```

**Gezielte Übung **Sende mit gleicher conversationId zuerst „Mein Name ist Ada“, danach „Wie heiße ich?“. Lösche den Verlauf mit DELETE und frage erneut.

**Worauf es ankommt **Das Fenster umfasst maximal 20 Nachrichten. Memory ist anwendungsseitig; das Modell wird dadurch nicht trainiert. Bei einem Fehler kann die bereits gespeicherte User Message bestehen bleiben.

## 2 ConversationMemoryController

POST /api/conversation-memory

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Gespräche durch eine ID getrennt halten.

**Didaktischer Sinn **ChatMemory.CONVERSATION_ID verbindet die Anfrage mit dem passenden Verlauf. Der Memory-Advisor ergänzt und speichert die Nachrichten automatisch.

```java
var answer = chatClient.prompt()
.user(message)
.advisors(MessageChatMemoryAdvisor.builder(memory).build())
.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
.call()
.content();
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Verwende zwei conversationIds mit unterschiedlichen Namen. Frage in beiden Gesprächen nach dem Namen.

**Worauf es ankommt **Der Standard demo ist für Übungen bequem, trennt aber keine Nutzer. Dieser Controller besitzt eine eigene Memory-Instanz, die nicht mit den anderen Controllern geteilt wird.

## 3 InMemoryRepositoryController

POST /api/in-memory-repository

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Repository und ChatMemory unterscheiden.

**Didaktischer Sinn **saveAll speichert eine vorgegebene Nachrichtenliste; anschließend werden IDs und gespeicherte Nachrichten gelesen. Der Endpunkt führt keinen Modellaufruf aus.

```java
repository.saveAll(conversationId,List.of(new UserMessage(message)));
return
Map.of("conversationIds",repository.findConversationIds(),"messages",repository.findByConversationId(conversationId));
```

**Gezielte Übung **Speichere zwei Texte nacheinander mit derselben conversationId und prüfe die gespeicherte Liste.

**Worauf es ankommt **saveAll ersetzt den Verlauf durch die übergebene Liste. Das Repository erzeugt keine Antwort, steuert kein Fenster und verliert seine Daten beim Neustart.

## 4 MemoryAdvisorController

POST /api/memory-advisor

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Die manuelle Memory-Logik durch einen Advisor ersetzen.

**Didaktischer Sinn **MessageChatMemoryAdvisor integriert das 20-Nachrichten-Fenster in die Verarbeitungskette. Der Vergleich mit ChatMemory reduziert sichtbare Infrastruktur im Controller.

```java
var answer = chatClient.prompt()
.user(message)
.advisors(MessageChatMemoryAdvisor.builder(memory).build())
.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
.call()
.content();
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Vergleiche die Requests im Proxy mit dem manuellen Beispiel. Beobachte vor und nach dem Aufruf die gespeicherten Nachrichten.

**Worauf es ankommt **Der Code gleicht ConversationMemory weitgehend. Der Schwerpunkt liegt hier auf der Advisor-Integration; beide Controller haben getrennte Memory-Instanzen.

## 5 MessageWindowController

POST /api/message-window

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Die Größe des gespeicherten Gesprächsfensters verstehen.

**Didaktischer Sinn **maxMessages(4) hält das Beispiel absichtlich klein. Verdrängung wird sichtbar, ohne lange Gespräche führen zu müssen.

```java
var answer = chatClient.prompt()
.user(message)
.advisors(MessageChatMemoryAdvisor.builder(memory).build())
.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
.call()
.content();
// Weitere Verarbeitung siehe Controller-Quellcode.
```

**Gezielte Übung **Nenne zu Beginn einen Namen und führe mehrere weitere Frage-Antwort-Paare. Prüfe die messages-Liste und frage erneut nach dem Namen.

**Worauf es ankommt **Die Grenze zählt Nachrichten, nicht Gesprächsrunden oder Tokens. Alte Fakten können aus dem Fenster fallen; es findet keine automatische Zusammenfassung statt.

## 6 PersistentMemoryController

POST /api/persistent-memory

Eingabe: Text im Body (Content-Type: text/plain); zusätzliche Query-Parameter gemäß Swagger.

**Lernziel **Gesprächsverlauf über Anwendungsneustarts erhalten.

**Didaktischer Sinn **Ein eigenes Cassandra-Repository wird an MessageWindowChatMemory angeschlossen. Die Advisor-Verwendung bleibt ähnlich, während sich die Speicherung ändert.

```java
var answer=chatClient.prompt()
.user(message)
.advisors(MessageChatMemoryAdvisor.builder(memory).build())
.advisors(a -> a.param(ChatMemory.CONVERSATION_ID,conversationId))
.call()
.content();
return Map.of("answer",answer,"messages",memory.get(conversationId));
```

**Gezielte Übung **Speichere einen Namen unter persistent-demo. Starte die Anwendung neu, lasse Cassandra bestehen und frage nach dem Namen.

**Worauf es ankommt **Das Trainingsrepository speichert Text und Rollen und ist für sequentielle Aufrufe vereinfacht. Löschen und erneutes Einfügen ist nicht atomar; Tool-Messages und konkurrierende Updates werden nicht vollständig behandelt.

## Transfer und Vertiefung

1. Welche Teile laufen in der Anwendung, welche im Modell und welche in einem externen Dienst? Ordne den wichtigsten Controller-Aufruf jeder Ebene zu.

2. Was zeigt die REST-Antwort, und welche zusätzlichen Informationen erhältst du im Debugger oder HTTP-Proxy? Unterscheide Datenstruktur, fachlichen Inhalt und technischen Ablauf.

3. Formuliere für zwei Controller eine passende und eine unpassende Eingabe. Erkläre, ob du eine andere Antwort, eine leere Ergebnismenge oder einen technischen Fehler erwartest.

4. Welche Aufgabe wird vom gezeigten Controller tatsächlich erledigt, welche wird nur vorbereitet? Identifiziere wiederverwendbare Komponenten und Unterschiede zur Produktionsimplementierung.

## Technische Referenz

Spring AI Reference, ChatMemory. Die Übungen verwenden die im Projekt eingebundenen APIs. Vertiefung:

https://docs.spring.io/spring-ai/reference/
