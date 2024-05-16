package database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import database.DatabaseHandler;
import database.models.Post;
import database.models.User;

public class PostRepository {
    public static void main(String[] args) {
        PostRepository postRepo = new PostRepository();

        // System.out.println(postRepo.getByFileName("3_11.png"));

        System.out.println(postRepo.getAllByUserId(1).size());

        // postRepo.savePost(UserRepository.getInstance().getById(1), "testubg", "jpg");
        // postRepo.deletePost(1, "19_1.jpg");

        System.out.println(postRepo.getAllByUserId(1).size());
    }

    private static PostRepository instance;

    private PostRepository() {
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }

        return instance;
    }

    public Post savePost(User user, String caption, String fileExtension) {
        try {
            int userId = user.getUserId();
            // int userId = 1;
            int postId = user.getPostsCount() + 1;
            // int postId = 2 + 1;
            String query = "INSERT INTO posts (posted_date, user_id, caption, image_path, likes_count, comments_count) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setInt(2, userId);
            // statement.setInt(2, 1);
            statement.setString(3, caption);
            statement.setString(4, "" + postId + "_" + userId
                    + "." + fileExtension);
            // statement.setString(4, "" + postId + "_" + userId
            // + ".jpg");
            statement.setInt(5, 0);
            statement.setInt(6, 0);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // very inefficient to have another query but more correct perchance
                return getLatestPostByUserId(userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deletePost(int userId, String fileName) {
        try {
            String query = "DELETE FROM posts WHERE user_id = ? AND image_path = ?";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, fileName);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Post getLatestPostByUserId(int userId) {
        try {
            String query = "SELECT * FROM posts WHERE user_id = ? ORDER BY posted_date DESC LIMIT 1";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return postFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Post getByFileName(String fileName) {
        try {
            String query = "SELECT * FROM posts WHERE image_path = ?";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setString(1, fileName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return postFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Post postFromResultSet(ResultSet resultSet) {
        try {
            int postId = resultSet.getInt("post_id");
            Date postedDate = resultSet.getTimestamp("posted_date");
            int userId = resultSet.getInt("user_id");
            String caption = resultSet.getString("caption");
            String imagePath = resultSet.getString("image_path");
            int likesCount = resultSet.getInt("likes_count");
            int commentsCount = resultSet.getInt("comments_count");

            return new Post(postId, postedDate, userId, caption, imagePath, likesCount, commentsCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Post getById(int id) {
        try {
            String query = "SELECT * FROM posts WHERE post_id = ?";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return postFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Post> getAllByUserId(int userId) {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            String query = "SELECT * FROM posts WHERE user_id = ?";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                posts.add(postFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public ArrayList<Post> getAll() {
        ArrayList<Post> posts = new ArrayList<>();
        try {
            String query = "SELECT * FROM posts";
            PreparedStatement statement = DatabaseHandler.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                posts.add(postFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
