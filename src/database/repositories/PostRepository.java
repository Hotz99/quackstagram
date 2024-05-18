package database.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import database.DatabaseHandler;
import database.models.Post;
import database.models.User;
import java.util.HashMap;
import java.util.Map;

public class PostRepository {

    private static Connection db;
    private static Map<Integer, Post> postCache;

    private static PostRepository instance;

    private PostRepository() {
        postCache = new HashMap<>();
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
            db = DatabaseHandler.getConnection();
        }

        return instance;
    }

    public Post savePost(User user, String caption, String fileExtension) {
        try {
            int userId = user.getUserId();

            // this function call ensures the postId will be up to date
            int postId = getPostsCountByUserId(userId) + 1;
            String query = "INSERT INTO posts (posted_date, user_id, caption, image_path, likes_count, comments_count) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = db.prepareStatement(query);

            Date postedDate = new Date();

            statement.setTimestamp(1, new Timestamp(postedDate.getTime()));
            statement.setInt(2, userId);
            statement.setString(3, caption);

            String imagePath = postId + "_" + userId + "." + fileExtension;

            statement.setString(4, imagePath);
            statement.setInt(5, 0);
            statement.setInt(6, 0);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                Post savedPost = new Post(postId, postedDate, userId, caption, imagePath, 0, 0);

                postCache.put(userId, savedPost);

                return savedPost;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Post getByPostId(int postId) {
        if (postCache.containsKey(postId)) {
            System.out.println("Post found in cache");
            return postCache.get(postId);
        }

        try {
            String query = "SELECT * FROM posts WHERE post_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, postId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = postFromResultSet(resultSet);
                postCache.put(postId, post);
                return post;
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
            PreparedStatement statement = db.prepareStatement(query);
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

    private int getPostsCountByUserId(int userId) {
        try {
            String query = "SELECT COUNT(*) FROM posts WHERE user_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deletePost(int postId) {
        if (postCache.containsKey(postId)) {
            postCache.remove(postId);
        }

        try {
            String query = "DELETE FROM posts WHERE post_id = ?";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, postId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // does not use cache to ensure the latest post is returned
    public Post getLatestPostByUserId(int userId) {
        try {
            String query = "SELECT * FROM posts WHERE user_id = ? ORDER BY posted_date DESC LIMIT 1";
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = postFromResultSet(resultSet);
                postCache.put(userId, post);
                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Post getByFileName(String fileName) {
        try {
            String query = "SELECT * FROM posts WHERE image_path = ?";
            PreparedStatement statement = db.prepareStatement(query);
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
}
