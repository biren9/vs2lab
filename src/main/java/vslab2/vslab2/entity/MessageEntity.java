package vslab2.vslab2.entity;

import java.util.Date;

public class MessageEntity {
    private Date timestamp;
    private String author;
    private String text;

    @Override
    public String toString() {
        return "MessageEntity{" +
                "timestamp=" + timestamp +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public MessageEntity() {

    }

    public MessageEntity(Date timestamp, String author, String text) {
        this.timestamp = timestamp;
        this.author = author;
        this.text = text;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
