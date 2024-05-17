package database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import database.DatabaseHandler;

public class LikeRepository {
    public static void main(String[] args) {
        LikeRepository likeRepository = new LikeRepository();

        int userId = 1;
        int postId = 1;

        System.out.println(likeRepository.hasUserLikedPost(userId, postId));

        likeRepository.toggleLike(userId, postId);

        System.out.println(likeRepository.hasUserLikedPost(userId, postId));

        // likeRepository.toggleLike(userId, postId);
    }

    private static LikeRepository instance;

    private LikeRepository() {
    }

    public static LikeRepository getInstance() {
        if (instance == null) {
            instance = new LikeRepository();
        }
        return instance;
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
        String query = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {

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
        String query = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {
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
        try (PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, postId);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

            System.out.println("added like");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
