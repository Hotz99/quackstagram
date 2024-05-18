package database.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import database.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeRepository {
    private static LikeRepository instance;
    private static Connection db;
    // postId -> userIds
    private static Map<Integer, List<Integer>> likesCache;

    private LikeRepository() {
        likesCache = new HashMap<>();
    }

    public static LikeRepository getInstance() {
        if (instance == null) {
            instance = new LikeRepository();
            db = DatabaseHandler.getConnection();
        }
        return instance;
    }

    public int getLikesCountByPostId(int postId) {
        if (likesCache.containsKey(postId)) {
            return likesCache.get(postId).size();
        }

        String query = "SELECT COUNT(*) FROM likes WHERE post_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean toggleLike(int postId, int userId) {
        if (hasUserLikedPost(postId, userId)) {
            removeLike(postId, userId);
        } else {
            addLike(postId, userId);
            return true;
        }

        return false;
    }

    private boolean hasUserLikedPost(int postId, int userId) {
        if (likesCache.containsKey(postId)) {
            return likesCache.get(postId).contains(userId);
        }

        String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, postId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void removeLike(int postId, int userId) {
        if (likesCache.containsKey(postId)) {
            List<Integer> updatedUserIds = likesCache.get(postId);
            updatedUserIds.remove(Integer.valueOf(userId));
            likesCache.put(postId, updatedUserIds);
        }

        String query = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, postId);
            statement.executeUpdate();

            System.out.println("removed like");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addLike(int postId, int userId) {
        String query = "INSERT INTO likes (user_id, post_id, liked_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, postId);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

            System.out.println("added like");

            if (likesCache.containsKey(postId)) {
                List<Integer> updatedUserIds = likesCache.get(postId);
                updatedUserIds.add(userId);
                likesCache.put(postId, updatedUserIds);
            } else {
                likesCache.put(postId, getLikesUserIds(postId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getLikesUserIds(int postId) {
        List<Integer> userIds = new ArrayList<>();

        String query = "SELECT user_id FROM likes WHERE post_id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userIds.add(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds;
    }
}
