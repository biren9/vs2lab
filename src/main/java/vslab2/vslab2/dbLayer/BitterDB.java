package vslab2.vslab2.dbLayer;

import vslab2.vslab2.entity.MessageEntity;

import java.util.List;
import java.util.Set;

public interface BitterDB {

    void generateTestData();

    Set<String> getUsersPageMatchingPattern(String pattern, int pageSize);

    void createUser(String username, String password);

    void deleteUser(String username);

    //security level : OVER 9000
    String getUserPassword(String username);

    Set<String> getSubs(String username);

    void addSub(String username, String subscribedUser);

    void removeSub(String username, String subscribedUser);

    void addMessage(String username, String text);

    List<String> getGlobalTimelineMessages(long start, long stop);

    List<String> getMessage(String username, long start, long stop);

    Set<String> getFollowers(String username);

    void addMessage(MessageEntity msg);

    void saveSession(String username, long minutesDuration, String uuid);

    void deleteSession(String sessionToken);

    String getUserBySessionToken(String token);

    List<String> getTimelineMessages(String username, long start, long stop);

}
