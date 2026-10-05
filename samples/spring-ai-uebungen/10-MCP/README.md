# 10-MCP: drei Applications

Unter org.javacream.training.spring.ai.mcp liegen die Unterpakete chat, server und client. Jede Application scannt nur ihr eigenes Unterpaket und aktiviert ihre Konfiguration automatisch.

| Paket | Application | Port | Aufgabe |
|---|---|---|---|
| server | ServerApplication | 8090 | MCP-Server und lokale REST-Demos |
| client | ClientApplication | 8190 | MCP-Discovery, Resources, Prompts; ohne Modellanbindung |
| chat | ChatApplication | 8290 | ChatClient, Ollama und eigener MCP-Client |

## Start in drei Terminals

Server zuerst starten. Keine zusätzlichen Profile angeben.

```bash
mvn spring-boot:run -Dapplication.mainClass=org.javacream.training.spring.ai.mcp.server.ServerApplication
```

```bash
mvn spring-boot:run -Dapplication.mainClass=org.javacream.training.spring.ai.mcp.client.ClientApplication
```

```bash
mvn spring-boot:run
```

Standard ist ChatApplication. Alternativ in der IDE die jeweilige main-Methode starten.

Server: /mcp, /api/mcp-server-tools, /api/mcp-server-resources, /api/mcp-annotations.
Client: /api/mcp-client, /api/mcp-resources, /api/mcp-prompts.
Chat: /api/mcp-tools.

Swagger UI jeweils unter http://localhost:<port>/swagger-ui.html. requests.http enthält passende URLs.

Die Server-REST-Demos rufen Java-Funktionen direkt auf. Client und Chat nutzen MCP zu localhost:8090/mcp. Nur Chat benötigt Ollama mit llama3.2. Der Proxy kann über OLLAMA_BASE_URL=http://localhost:11435 verwendet werden; seine Weboberfläche bleibt auf 9082.

mvn package erstellt standardmäßig ein Chat-JAR. Mit -Dapplication.mainClass=<Klassenname> kann ein anderer Einstiegspunkt paketiert werden.
