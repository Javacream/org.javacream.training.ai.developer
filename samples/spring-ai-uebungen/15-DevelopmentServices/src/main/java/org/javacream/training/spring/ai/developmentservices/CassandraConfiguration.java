package org.javacream.training.spring.ai.developmentservices;
import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.*;
import org.springframework.boot.cassandra.autoconfigure.CassandraConnectionDetails;
import java.net.InetSocketAddress;
@Configuration
public class CassandraConfiguration {
 @Bean(destroyMethod="close") CqlSession session(CassandraConnectionDetails details) {
  var builder=CqlSession.builder().withLocalDatacenter("datacenter1");
  for(var node:details.getContactPoints()) builder.addContactPoint(new InetSocketAddress(node.host(),node.port()));
  return builder.build();
 }
}
