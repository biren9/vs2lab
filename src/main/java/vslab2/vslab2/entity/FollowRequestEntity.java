package vslab2.vslab2.entity;

public class FollowRequestEntity {
    @Override
    public String toString() {
        return "FollowRequestEntity{" +
                "follow='" + follow + '\'' +
                '}';
    }

    private String follow;

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }
}
