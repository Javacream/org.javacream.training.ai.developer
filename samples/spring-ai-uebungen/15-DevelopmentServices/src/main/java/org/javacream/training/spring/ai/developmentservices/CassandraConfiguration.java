package org.javacream.training.spring.ai.developmentservices;

import java.net.InetSocketAddress;

import org.springframework.boot.cassandra.autoconfigure.CassandraConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfiguration {
	@Bean(destroyMethod = "close")
	CqlSession session(CassandraConnectionDetails details) {
		var builder = CqlSession.builder().withLocalDatacenter("datacenter1");
		for (var node : details.getContactPoints())
			builder.addContactPoint(new InetSocketAddress(node.host(), node.port()));
		return builder.build();
	}
}
