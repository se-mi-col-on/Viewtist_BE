package semicolon.viewtist.config;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class RedisConfigTest {
  @Autowired
  private  StringRedisTemplate redisTemplate;

  @Test
  void testStrings(){
      // given
    final String key = "newkey";
    final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
      // when
    stringStringValueOperations.set(key,"1");
    final String result= stringStringValueOperations.get(key);
      // then
    assertEquals("1",result);
  }
@Test
  void testHash(){
    // given
    String key = "chatRoomSession";
    HashOperations<String, Object,Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
    stringObjectObjectHashOperations.put(key, "session1","chatroom1");
    stringObjectObjectHashOperations.put(key, "session2","chatroom2");
    stringObjectObjectHashOperations.put(key, "session3","chatroom1");
    // when
    Map<Object,Object> entries = stringObjectObjectHashOperations.entries(key);

    // then
    assertEquals(3,entries.size());
    assertEquals("chatroom1",entries.get("session3"));
  }

}