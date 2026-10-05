# Spring Boot AI Restservice:
* curl -X 'POST' 'http://localhost:8081/api/chat-clientresponse' -H 'accept: */*' -H 'Content-Type: application/json' -d 'Was ist die Haupstadt Deutschlands'

# Direkter Ollama-Zugriff:
* curl http://localhost:11434/api/chat -H "Content-Type: application/json" -d '{"model": "llama3.2", "messages": [{"role": "user","content": "Was ist die Haupstadt Deutschlands"}],"stream": false}'
