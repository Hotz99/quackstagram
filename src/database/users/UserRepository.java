package database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DatabaseHandler;
import user.User;

public class UserRepository {

    public static void main(String[] args) {
        UserRepository userRepo = UserRepository.getInstance();

        // System.out.println(userRepo.getByUsername("user1"));

        User fakeUser = new User(0, new Date(), "fakeuser", "password123", "/path/to/profile/image.jpg",
                "This is a fake user", 0, 0, 0);
        // UserRepository.getInstance().save(fakeUser);

        System.out.println(userRepo.getByUsername("fakeuser"));

        // for (User user : userRepo.getAll()) {
        // System.out.println(user.getUsername());
        // }
    }

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository(DatabaseHandler.getConnection());
        }

        return instance;
    }

    private Connection connection;

    private UserRepository(Connection connection) {
        this.connection = connection;
    }

    private static User userFromResultSet(ResultSet resultSet) throws SQLException {
        try {
            int userId = resultSet.getInt("user_id");
            Date createdDate = resultSet.getTimestamp("created_date");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String profileImagePath = resultSet.getString("profile_image_path");
            String bio = resultSet.getString("bio");
            int postsCount = resultSet.getInt("posts_count");
            int followersCount = resultSet.getInt("followers_count");
            int followingCount = resultSet.getInt("following_count");

            return new User(userId, createdDate, username, password, profileImagePath, bio, postsCount,
                    followersCount, followingCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getById(int userId) {
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return userFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getByUsername(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return userFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(userFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void save(User user) {
        if (getByUsername(user.getUsername()) != null) {
            System.out.println("failed to save user to database: username already exists");
            return;
        }

        try {
            String query = "INSERT INTO users (created_date, username, password, profile_image_path, bio, posts_count, followers_count, following_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, new Timestamp(user.getCreatedDate().getTime()));
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getProfileImagePath());
            statement.setString(5, user.getBio());
            statement.setInt(6, user.getPostsCount());
            statement.setInt(7, user.getFollowersCount());
            statement.setInt(8, user.getFollowingCount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        // Implementation using JDBC
    }

    public void delete(int id) {
        // Implementation using JDBC
    }
}