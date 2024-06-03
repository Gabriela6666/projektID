package viewandmodels.filmy;

public class UserBadge {
    private int userId;
    private String badge;

    public UserBadge(int userId, String badge) {
        this.userId = userId;
        this.badge = badge;
    }

    public int getUserId() {
        return userId;
    }

    public String getBadge() {
        return badge;
    }
}
