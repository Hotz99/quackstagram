package database;

import post.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PostRepository {
    public static void main(String[] args) {
        PostRepository postRepo = new PostRepository();

        System.out.println(postRepo.getById(1));

        for (Post post : postRepo.getAllByUserId(9)) {
            System.out.println(post);
        }
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
