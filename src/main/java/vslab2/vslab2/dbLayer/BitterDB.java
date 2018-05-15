package vslab2.vslab2.dbLayer;

import java.util.List;
import java.util.Set;

public interface BitterDB {

    void testConn();

    void createUser(String username, String password);

    void deleleteUser(String username);

    //security level : OVER 9000
    String getUserPassword(String username);

    Set<String> getSubs(String username);

    void addSub(String username, String subscribedUser);

    void removeSub(String username, String subscribedUser);

    void addMessage(String username, String text);

    Set<String> getFollowers(String username);
}