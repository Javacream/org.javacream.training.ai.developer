package org.javacream.training.spring.ai.chatmemory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
@Component
public class TrainingCassandraChatMemoryRepository implements ChatMemoryRepository {
 private final CqlSession session;
 public TrainingCassandraChatMemoryRepository(CqlSession session) {
  this.session=session;
  session.execute("CREATE TABLE IF NOT EXISTS spring_ai.chat_memory (conversation_id text, position int, role text, content text, PRIMARY KEY (conversation_id, position))");
 }
 public List<String> findConversationIds() {
  var ids=new HashSet<String>();session.execute("SELECT conversation_id FROM spring_ai.chat_memory").forEach(r -> ids.add(r.getString("conversation_id")));return new ArrayList<>(ids);
 }
 public List<Message> findByConversationId(String id) {
  var rows=session.execute(session.prepare("SELECT role,content FROM spring_ai.chat_memory WHERE conversation_id=?").bind(id));
  List<Message> messages=new ArrayList<>();
  for(var r:rows) { String text=r.getString("content");messages.add(switch(r.getString("role")) { case "SYSTEM" -> new SystemMessage(text);case "ASSISTANT" -> new AssistantMessage(text);default -> new UserMessage(text); }); }
  return messages;
 }
 public void saveAll(String id,List<Message> messages) {
  deleteByConversationId(id);var statement=session.prepare("INSERT INTO spring_ai.chat_memory (conversation_id,position,role,content) VALUES (?,?,?,?)");
  for(int i=0;i<messages.size();i++) { var m=messages.get(i);session.execute(statement.bind(id,i,m.getMessageType().name(),m.getText())); }
 }
 public void deleteByConversationId(String id) { session.execute(session.prepare("DELETE FROM spring_ai.chat_memory WHERE conversation_id=?").bind(id)); }
}
