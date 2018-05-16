package vslab2.vslab2.dbLayer;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public class BitterDBImpl implements BitterDB {
    private Jedis jedis = new Jedis("localhost");
    private static final String BITTER_USERS_SET = "users";
    private static final String BITTER_SUBSCRIPTIONS_PREFIX = "subs:";
    private static final String BITTER_FOLLOWERS_PREFIX = "followers:";
    private static final String BITTER_MESSAGES_PREFIX = "msgs:";


    @Override
    public void generateTestData() {
        createUser("pknp", "pknp");
        for (int i = 0; i<20; i++) {
            addMessage("pknp", "Nachricht" + 1);
        }
    }
    
    @Override
    public void createUser(String username, String password) {
        Map<String, String> attr = new HashMap<>();
        //saving passwords in plaintext ¯\_(ツ)_/¯
        attr.put("password", password);
        jedis.hmset(username, attr);
        jedis.sadd(BITTER_USERS_SET, username);
    }

    @Override
    public void deleleteUser(String username) {
        jedis.del(username);
        jedis.srem(username);
    }

    //security level : OVER 9000
    //TODO: hash passwords
    @Override
    public String getUserPassword(String username) {
        List<String> pass = jedis.hmget(username, "password");
        if ((pass == null) || (pass.isEmpty())) {
            return "";
        }
        return pass.get(0);
    }
    
    @Override
    public Set<String> getSubs(String username) {
        return jedis.smembers(BITTER_SUBSCRIPTIONS_PREFIX + username);
    }

    @Override
    public void addSub(String username, String subscribedUser) {
        jedis.sadd(BITTER_SUBSCRIPTIONS_PREFIX + username, subscribedUser);
        jedis.sadd(BITTER_FOLLOWERS_PREFIX + subscribedUser, username);
    }
    
    @Override
    public void removeSub(String username, String subscribedUser) {
        jedis.srem(BITTER_SUBSCRIPTIONS_PREFIX + username, subscribedUser);
        jedis.srem(BITTER_FOLLOWERS_PREFIX + subscribedUser, username);
    }

    @Override
    public void addMessage(String username, String text) {
        jedis.lpush(BITTER_MESSAGES_PREFIX + username, text);
    }

    @Override
    public List<String> getMessage(String username, long start, long stop) {
        return jedis.lrange(username, start, stop);
    }
    
    @Override
    public Set<String> getFollowers(String username) {
        return jedis.smembers(BITTER_FOLLOWERS_PREFIX + username);
    }
    
}
