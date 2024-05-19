package database.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DatabaseHandler;
import database.models.User;

public class UserRepository {

    private static Connection db;

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
            db = DatabaseHandler.getConnection();
        }

        return instance;
    }

    public User create(String username, String password, String bio) {
        if (getByUsername(username) != null) {
            System.out.println("failed to save user to database: username already exists");
            return null;
        }

        try {
            User newUser = new User(0, new Date(), username, password,
                    "default.jpg", bio, 0, 0, 0);

            String query = "INSERT INTO users (created_date, username, password, profile_image_path, bio, posts_count, followers_count, following_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setTimestamp(1, new Timestamp(newUser.getCreatedDate().getTime()));
            statement.setString(2, newUser.getUsername());
            statement.setString(3, newUser.getPassword());
            statement.setString(4, newUser.getProfileImagePath());
            statement.setString(5, newUser.getBio());
            statement.setInt(6, newUser.getPostsCount());
            statement.setInt(7, newUser.getFollowersCount());
            statement.setInt(8, newUser.getFollowingCount());
            statement.executeUpdate();

            return newUser;
        } catch (SQLException e) {
            System.out.println("failed to save user to database");
            e.printStackTrace();
        }

        return null;
    }

    public List<String> fuzzyFindUsernames(String username) {
        List<String> usernames = new ArrayList<>();

        try {
            String query = "SELECT * FROM users WHERE username LIKE ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setString(1, "%" + username + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usernames.add(extractUserFromResultSet(resultSet).getUsername());
            }

        } catch (SQLException e) {
            System.out.println("failed to fuzzy find username: " + username);
            e.printStackTrace();
        }

        return usernames;
    }

    public User getByUserId(int userId) {
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("failed to get user by id: " + userId);
            e.printStackTrace();
        }

        return null;
    }

    // usernames are unique
    public User getByUsername(String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("failed to get user by username: " + username);
            e.printStackTrace();
        }

        return null;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            PreparedStatement statement = db.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("failed to get all users");
            e.printStackTrace();
        }
        return users;
    }

    public void update(User user) {
        try {
            String query = "UPDATE users SET username = ?, password = ?, profile_image_path = ?, bio = ?, posts_count = ?, followers_count = ?, following_count = ? WHERE user_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getProfileImagePath());
            statement.setString(4, user.getBio());
            statement.setInt(5, user.getPostsCount());
            statement.setInt(6, user.getFollowersCount());
            statement.setInt(7, user.getFollowingCount());
            statement.setInt(8, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("failed to update user with id: " + user.getUserId());
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try {
            String query = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("failed to delete user with id: " + userId);
            e.printStackTrace();
        }
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
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
            System.out.println("failed to extract user from result set");
            e.printStackTrace();
        }

        return null;
    }
}