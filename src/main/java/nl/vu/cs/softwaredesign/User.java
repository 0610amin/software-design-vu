package nl.vu.cs.softwaredesign;

public class User {
    private final String id;
    private final UserProfile profile;
    private final UserPreferences preferences;

    public User(String id, UserProfile profile, UserPreferences preferences) {
        this.id = id;
        this.profile = profile;
        this.preferences = preferences;
    }

    public String getId() {
        return id;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public UserPreferences getPreferences() {
        return preferences;
    }
}