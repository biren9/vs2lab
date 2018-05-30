package vslab2.vslab2.dbLayer;

import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;
import reactor.util.annotation.Nullable;
import vslab2.vslab2.controller.UserListController;
import vslab2.vslab2.entity.MessageEntity;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Repository
public class BitterDBImpl implements BitterDB {

    private final RedisTemplate<String, String> template;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ListOperations<String, String> listOps;
    private SetOperations<String, String> setOps;
    private HashOperations<String, String, String> hashOps;
    private ValueOperations<String, String> valOps;

    @Autowired
    public BitterDBImpl(RedisTemplate<String, String> template, Gson gson) {
        this.template = template;
        this.gson = gson;
    }

    @PostConstruct
    private void init() {
        valOps = template.opsForValue();
        listOps = template.opsForList();
        setOps = template.opsForSet();
        hashOps = template.opsForHash();
        generateTestData();
    }


    private final Gson gson;

    private static final String BITTER_USERS_SET = "users";
    private static final String BITTER_SUBSCRIPTIONS_PREFIX = "subs:";
    private static final String BITTER_FOLLOWERS_PREFIX = "followers:";
    private static final String BITTER_MESSAGES_PREFIX = "msgs:";
    private static final String BITTER_TIMELINE_PREFIX = "timeline:";
    private static final String BITTER_GLOBAL_TIMELINE = "global_timeline";


    @Override
    public void generateTestData() {
        template.getConnectionFactory().getConnection().serverCommands().flushAll();
        addSub("pknp", "pknp");
        createUser("pknp", "pknp");
        for (int i = 0; i < 20; i++) {
            addMessage("pknp", "Nachricht" + i);
            createUser("test" + i, "test");
        }
    }

    //possible REGEX DDOS ¯\_(ツ)_/¯
    //TODO: escape pattern
    /**
     *
     * @param pattern search for users with pattern as prefix.
     * @param pageSize amount distinct users to be returned.
     * @return a page of users.
     */
    @Override
    public Set<String> getUsersPageMatchingPattern(@Nullable String pattern, int pageSize, int pageNumber) {
        ScanOptions options = (pattern != null) ?
                ScanOptions.scanOptions().match(pattern + "*").build()
                : ScanOptions.scanOptions().build();
        Set<String> result = new HashSet<>();
        Cursor c = setOps.scan("users", options);
        try {
            int resultsToSkip = UserListController.USERS_LIST_PAGE_SIZE * pageNumber;
            while(c.hasNext() && resultsToSkip-- > 0) {
                c.next();
            }
            while (c.hasNext() && (result.size() != pageSize)) {
                result.add((String) c.next());
            }
        } catch (NoSuchElementException nse) {
            log.debug("search users page with pattern failed");
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                log.error("IOException while getting users");
            }
        }
        return result;
    }

    @Override
    public void createUser(String username, String password) {
        Map<String, String> attr = new HashMap<>();
        //saving passwords in plaintext ¯\_(ツ)_/¯
        hashOps.put(username, "password", password);
        setOps.add(BITTER_USERS_SET, username);
    }

    @Override
    public void deleteUser(String username) {
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
        addMessage(message);
    }

    @Override
    public void addMessage(MessageEntity msg) {
        String author = msg.getAuthor();
        for (String follower : getFollowers(author)) {
            listOps.leftPush(BITTER_TIMELINE_PREFIX + follower, gson.toJson(msg));
        }
        listOps.leftPush(BITTER_GLOBAL_TIMELINE, gson.toJson(msg));
        listOps.leftPush(BITTER_MESSAGES_PREFIX + author, gson.toJson(msg));

    }

    @Override
    public void saveSession(String username, long minutesDuration, String uuid) {
        valOps.set(uuid, username, minutesDuration, TimeUnit.MINUTES);
    }

    @Override
    public void deleteSession(String sessionToken) {
        template.delete(sessionToken);
    }

    @Override
    public String getUserBySessionToken(String token) {
        return valOps.get(token);
    }

    @Override
    public List<String> getTimelineMessages(String username, long start, long stop) {
        return listOps.range(BITTER_TIMELINE_PREFIX + username, start, stop);
    }

    @Override
    public List<String> getGlobalTimelineMessages(long start, long stop) {
        return listOps.range(BITTER_GLOBAL_TIMELINE, start, stop);
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
