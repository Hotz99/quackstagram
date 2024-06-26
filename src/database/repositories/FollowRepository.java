package database.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseHandler;

public class FollowRepository {

    private static Connection db;

    private static FollowRepository instance;

    private FollowRepository() {
    }

    public static FollowRepository getInstance() {
        if (instance == null) {
            instance = new FollowRepository();
            db = DatabaseHandler.getConnection();
        }

        return instance;
    }

    public List<Integer> getFollowedByUserId(int userId) {
        String query = "SELECT followed_id FROM follows WHERE follower_id = ?";
        List<Integer> followedIds = new ArrayList<>();

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                followedIds.add(resultSet.getInt("followed_id"));
            }
        } catch (SQLException e) {
            System.out.println("failed to get followed users by user id");
            e.printStackTrace();
        }

        return followedIds;
    }

    public boolean toggleFollow(int userId, int otherUserId) {
        if (doesUserFollowOtherUser(userId, otherUserId)) {
            removeFollow(userId, otherUserId);
        } else {
            addFollow(userId, otherUserId);
            return true;
        }
        return false;
    }

    public boolean doesUserFollowOtherUser(int userId, int otherUserId) {
        String query = "SELECT COUNT(*) FROM follows WHERE follower_id = ? AND followed_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, otherUserId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("failed to check if user follows other user");
            e.printStackTrace();
        }
        return false;
    }

    private void removeFollow(int followerId, int followedId) {
        String query = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followedId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("failed to remove follow");
            e.printStackTrace();
        }
    }

    private void addFollow(int followerId, int followedId) {
        String query = "INSERT INTO follows (follower_id, followed_id, followed_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followedId);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("failed to add follow");
            e.printStackTrace();
        }
    }
}
