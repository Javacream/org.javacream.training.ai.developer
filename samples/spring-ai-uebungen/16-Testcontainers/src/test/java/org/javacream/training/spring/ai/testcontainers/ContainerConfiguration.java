package org.javacream.training.spring.ai.testcontainers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.wait.strategy.Wait;
import java.time.Duration;
import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.cassandra.CassandraVectorStore;
@TestConfiguration(proxyBeanMethods=false)
public class ContainerConfiguration {
 @Bean(destroyMethod="stop") GenericContainer<?> cassandraContainer() {
  var container=new GenericContainer<>(DockerImageName.parse("cassandra:5.0.6"))
   .withExposedPorts(9042).withEnv("CASSANDRA_DC","datacenter1")
   .waitingFor(Wait.forLogMessage(".*Startup complete.*\\n",1)).withStartupTimeout(Duration.ofMinutes(5));
  container.start();return container;
 }
 @Bean(destroyMethod="close") CqlSession session(GenericContainer<?> cassandraContainer) {
  var session=CqlSession.builder().addContactPoint(new InetSocketAddress(cassandraContainer.getHost(),cassandraContainer.getMappedPort(9042))).withLocalDatacenter("datacenter1").build();
  session.execute("CREATE KEYSPACE IF NOT EXISTS spring_ai WITH replication = {'class':'SimpleStrategy','replication_factor':1}");return session;
 }
 @Bean VectorStore vectorStore(CqlSession session,EmbeddingModel embeddingModel) {
  return CassandraVectorStore.builder(embeddingModel).session(session).keyspace("spring_ai").table("vectors").initializeSchema(true).build();
 }
}
