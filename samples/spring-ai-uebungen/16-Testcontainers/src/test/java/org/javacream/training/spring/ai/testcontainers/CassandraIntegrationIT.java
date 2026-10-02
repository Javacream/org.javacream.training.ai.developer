package org.javacream.training.spring.ai.testcontainers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT, properties="training.cassandra.enabled=false")
@Import(ContainerConfiguration.class)
class CassandraIntegrationIT {
 @Value("${local.server.port}") int port;
 @Test void endpointStoresAndRetrievesDocument() {
  String response=RestClient.create("http://localhost:"+port).post().uri("/api/integration-test").contentType(MediaType.TEXT_PLAIN).body("Cassandra supports vector similarity searches.").retrieve().body(String.class);
  assertThat(response).contains("id").contains("results").contains("Cassandra supports vector similarity searches");
 }
}
