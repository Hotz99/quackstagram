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
    public final String IMG;
    public final String UPLOADED;
    public final String LOGOS;
    public final String DACS_LOGO;
    public final String IMAGE_DETAILS;
    public final String PROFILE_IMAGES_STORAGE;
    public final String ICONS;
    public final String DEFAULT_PROFILE_ICON;
    public final String[] SEARCH_PATHS;
    public final String[] ICON_PATHS;
    public final String[] BUTTON_TYPES;

    private AppPathsSingleton() {
    
        DATA = "resources/data/";
        CREDENTIALS =  "resources/data/credentials.txt";
        FOLLOWING =  "resources/data/following.txt";
        FOLLOWERS =  "resources/data/followers.txt";
        USERS =  "resources/data/users.txt";
        LIKES =  "resources/data/likes.txt";
        NOTIFICATIONS = "resources/data/notifications.txt";
        IMG = "resources/img/";
        UPLOADED = "resources/img/uploaded/";
        LOGOS =  "resources/img/logos/";
        DACS_LOGO =  "resources/img/logos/DACS.png";
        IMAGE_DETAILS =  "resources/img/image_details.txt";
        PROFILE_IMAGES_STORAGE = "resources/img/storage/profile/";
        ICONS =  "resources/img/icons/";
        DEFAULT_PROFILE_ICON = "resources/img/icons/default_profile.png";
        SEARCH_PATHS = new String[]{
        "resources/data/users.txt", 
        "resources/img/image_details.txt",
        };
        ICON_PATHS = new String []{
        "resources/img/icons/home.png",
        "resources/img/icons/search.png",
        "resources/img/icons/add.png",
        "resources/img/icons/heart.png",
        "resources/img/icons/profile.png"
        };
        BUTTON_TYPES = new String[]{
            "home",
            "explore",
            "add",
            "notification",
            "profile",
        };
    }

public static AppPathsSingleton getInstance(){
    if (instance == null){
        instance = new AppPathsSingleton();
    }
    return instance;
}
   
}
