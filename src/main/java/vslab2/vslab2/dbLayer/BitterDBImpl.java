package vslab2.vslab2.dbLayer;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;
import vslab2.vslab2.entity.MessageEntity;

import javax.annotation.PostConstruct;
import java.util.*;


@Repository
public class BitterDBImpl implements BitterDB {

    @Autowired
    private RedisTemplate<String, String> template;

    private ListOperations<String, String> listOps;
    private SetOperations<String, String> setOps;
    private HashOperations<String, String, String> hashOps;

    @PostConstruct
    private void init() {
        listOps = template.opsForList();
        setOps = template.opsForSet();
        hashOps = template.opsForHash();
    }


    @Autowired
    private Gson gson;

    private static final String BITTER_USERS_SET = "users";
    private static final String BITTER_SUBSCRIPTIONS_PREFIX = "subs:";
    private static final String BITTER_FOLLOWERS_PREFIX = "followers:";
    private static final String BITTER_MESSAGES_PREFIX = "msgs:";


    @Override
    public void generateTestData() {
        createUser("pknp", "pknp");
        template.getConnectionFactory().getConnection().serverCommands().flushAll();
        for (int i = 0; i<20; i++) {
            addMessage("pknp", "Nachricht" + i);
        }
    }
    
    @Override
    public void createUser(String username, String password) {
        Map<String, String> attr = new HashMap<>();
        //saving passwords in plaintext ¯\_(ツ)_/¯
        hashOps.put(username, "password", password);
        setOps.add(BITTER_USERS_SET, username);
    }

    @Override
    public void deleleteUser(String username) {
        template.delete(username);
        setOps.remove(BITTER_USERS_SET, username);
    }

    //security level : OVER 9000
    //TODO: hash passwords
    @Override
    public String getUserPassword(String username) {
        return hashOps.get(username, "password");
    }
    
    @Override
    public Set<String> getSubs(String username) {
        return setOps.members(BITTER_SUBSCRIPTIONS_PREFIX + username);
    }

    @Override
    public void addSub(String username, String subscribedUser) {
        setOps.add(BITTER_SUBSCRIPTIONS_PREFIX + username, subscribedUser);
        setOps.add(BITTER_FOLLOWERS_PREFIX + subscribedUser, username);
    }
    
    @Override
    public void removeSub(String username, String subscribedUser) {
        setOps.remove(BITTER_SUBSCRIPTIONS_PREFIX + username, subscribedUser);
        setOps.remove(BITTER_FOLLOWERS_PREFIX + subscribedUser, username);
    }

    @Override
    public void addMessage(String username, String text) {
        MessageEntity message = new MessageEntity(new Date(), username, text);
        listOps.leftPush(BITTER_MESSAGES_PREFIX + username,  gson.toJson(message));
    }

    @Override
    public List<String> getMessage(String username, long start, long stop) {
        return listOps.range(BITTER_MESSAGES_PREFIX + username, start, stop);
    }
    
    @Override
    public Set<String> getFollowers(String username) {
        return setOps.members(BITTER_FOLLOWERS_PREFIX + username);
    }
    
}
