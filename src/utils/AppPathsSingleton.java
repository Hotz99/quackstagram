package utils;

public class AppPathsSingleton {

    private static AppPathsSingleton instance;

    public final String DATA;
    public final String CREDENTIALS;
    public final String FOLLOWING;
    public final String FOLLOWERS;
    public final String USERS;
    public final String LIKES;
    public final String NOTIFICATIONS;
    public final String IMAGES;
    public final String UPLOADED;
    public final String LOGOS;
    public final String DACS_LOGO;
    public final String IMAGE_DETAILS;
    public final String PROFILE_IMAGES;
    public final String ICONS;
    public final String DEFAULT_PROFILE_IMAGE;
    public final String[] SEARCH_PATHS;
    public final String[] ICON_PATHS;
    public final String[] BUTTON_TYPES;

    private AppPathsSingleton() {
        DATA = "resources/data/";
        CREDENTIALS = "resources/data/credentials.txt";
        FOLLOWING = "resources/data/following.txt";
        FOLLOWERS = "resources/data/followers.txt";
        USERS = "resources/data/users.txt";
        LIKES = "resources/data/likes.txt";
        NOTIFICATIONS = "resources/data/notifications.txt";
        IMAGES = "resources/images/";
        UPLOADED = "resources/images/uploaded/";
        LOGOS = "resources/images/logos/";
        DACS_LOGO = "resources/images/logos/DACS.png";
        IMAGE_DETAILS = "resources/images/image_details.txt";
        PROFILE_IMAGES = "resources/images/profile/";
        ICONS = "resources/images/icons/";
        DEFAULT_PROFILE_IMAGE = "resources/images/profile/default.jpg";
        SEARCH_PATHS = new String[] {
                "resources/data/users.txt",
                "resources/images/image_details.txt",
        };
        ICON_PATHS = new String[] {
                "resources/images/icons/home.png",
                "resources/images/icons/search.png",
                "resources/images/icons/add.png",
                "resources/images/icons/heart.png",
                "resources/images/icons/profile.png"
        };
        BUTTON_TYPES = new String[] {
                "home",
                "explore",
                "add",
                "notification",
                "profile",
        };
    }

    public static AppPathsSingleton getInstance() {
        if (instance == null) {
            instance = new AppPathsSingleton();
        }
        return instance;
    }

}
