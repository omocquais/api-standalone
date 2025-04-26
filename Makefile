java:
	@echo  "Current Java version"
	java --version
clean:
	@echo  "Cleaning ..."
	./mvnw clean
build:
	@echo  "Build the application"
	./mvnw package
	@echo "DONE"
start:
	@echo  "Start the application"
	java -jar target/api-standalone-0.0.1-SNAPSHOT.jar

start-api-mock:
	java -jar wiremock/wiremock-standalone-3.13.0.jar --port 9999 --verbose --root-dir=wiremock

test-api-mock:
	curl http://localhost:9999/api/products | jq .

products-list:
	curl http://localhost:8080/products | jq .

product:
	curl http://localhost:8080/products/3 | jq .

basket:
	curl -d '@basket-request.json' -H "Content-Type: application/json" -X POST http://localhost:8080/basket | jq .
