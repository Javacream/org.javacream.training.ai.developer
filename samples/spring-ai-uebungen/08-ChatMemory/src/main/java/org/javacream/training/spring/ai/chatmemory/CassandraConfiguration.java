package org.javacream.training.spring.ai.chatmemory;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfiguration {
	@Bean(destroyMethod = "close")
	CqlSession session(@Value("${training.cassandra.host:localhost}") String host,
			@Value("${training.cassandra.port:9042}") int port,
			@Value("${training.cassandra.datacenter:datacenter1}") String dc) {
		var session = CqlSession.builder().addContactPoint(new InetSocketAddress(host, port)).withLocalDatacenter(dc)
				.build();
		session.execute(
				"CREATE KEYSPACE IF NOT EXISTS spring_ai WITH replication = {'class':'SimpleStrategy','replication_factor':1}");
		return session;
	}
}
