package vslab2.vslab2.entity;

import java.util.List;
import java.util.Set;

public class MessagePageEntity {
    Set<String> subs;

    public MessagePageEntity(Set<String> subs, List<String> messages) {
        this.subs = subs;
        this.messages = messages;
    }

    List<String> messages;

    public Set<String> getSubs() {
        return subs;
    }

    public void setSubs(Set<String> subs) {
        this.subs = subs;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
