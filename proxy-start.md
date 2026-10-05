# Verwenden des http-Proxies

* docker compose -f proxy-docker-compose.yml -d
* Ändern in der Spring Boot-Konfiguration von http://localhost:11434 http://localhost:11435
* Abfragen wie gehabt
* Im Browser öffne http://localhost:9092, authentifiziere mit training