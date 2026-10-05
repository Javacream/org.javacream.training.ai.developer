package org.javacream.training.spring.ai.vectorstores;

import java.net.InetSocketAddress;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.cassandra.CassandraVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
@ConditionalOnProperty(name = "training.cassandra.enabled", havingValue = "true", matchIfMissing = true)
public class CassandraConfiguration {
	@Bean(destroyMethod = "close")
	CqlSession session(@Value("${training.cassandra.host:localhost}") String host,
			@Value("${training.cassandra.port:9042}") int port,
			@Value("${training.cassandra.datacenter:datacenter1}") String dc) {
		return CqlSession.builder().addContactPoint(new InetSocketAddress(host, port)).withLocalDatacenter(dc).build();
	}

	@Bean
	VectorStore vectorStore(CqlSession session, EmbeddingModel embeddingModel,
			@Value("${training.cassandra.keyspace:spring_ai}") String keyspace) {
		session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace
				+ " WITH replication = {'class':'SimpleStrategy','replication_factor':1}");
		return CassandraVectorStore.builder(embeddingModel).session(session).keyspace(keyspace).table("vectors")
				.addMetadataColumns(new org.springframework.ai.vectorstore.cassandra.CassandraVectorStore.SchemaColumn(
						"category", com.datastax.oss.driver.api.core.type.DataTypes.TEXT,
						org.springframework.ai.vectorstore.cassandra.CassandraVectorStore.SchemaColumnTags.INDEXED))
				.initializeSchema(true).build();
	}
}
