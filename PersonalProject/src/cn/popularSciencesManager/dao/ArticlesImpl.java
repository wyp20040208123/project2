package cn.popularSciencesManager.dao;

import cn.popularSciencesManager.model.*;
import com.mysql.cj.xdevapi.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticlesImpl implements ArticlesDao{
    static String databaseName = "sciences";
    static String URL = "127.0.0.1:3306";
    static String url = "jdbc:mysql://" + URL + "/" + databaseName + "?serverTimezone=GMT%2B8  &  useSSL=true & characterEncoding=utf8";
    static String username = "root";
    static String password = "12345600O";
    private static final String intser = "INSERT INTO articles (title, content, createTime, type) VALUES (?, ?, ?, ?)";
    private static final String update = "UPDATE articles SET title = ?, content = ?, createTime = ?, type = ? WHERE articleID = ?";
    private static final String delete = "DELETE FROM articles WHERE articleID = ?";
    private static final String select = "SELECT * FROM articles WHERE articleID = ?";
    private static final String selectAll = "SELECT * FROM articles";

    @Override
    public int insertArticle(ArticlesModel article) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(intser)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getCreateTime());
            pstmt.setString(4, article.getType());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return affectedRows;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateArticle(ArticlesModel article) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(update)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getCreateTime());
            pstmt.setString(4, article.getType());
            pstmt.setInt(5, article.getArticleID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return affectedRows;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteArticle(int articleID) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(delete)) {

            pstmt.setInt(1, articleID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return affectedRows;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public ArticlesModel getArticle(int articleID) {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(select)) {

            pstmt.setInt(1, articleID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ArticlesModel article = new ArticlesModel();
                    article.setArticleID(rs.getInt("articleID"));
                    article.setTitle(rs.getString("title"));
                    article.setContent(rs.getString("content"));
                    article.setCreateTime(rs.getString("createTime"));
                    article.setType(rs.getString("type"));
                    return article;
                } else {
                    return null; //未找到
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ArticlesModel> getAllArticles() {
        List<ArticlesModel> articles = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(selectAll);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ArticlesModel article = new ArticlesModel();
                article.setArticleID(rs.getInt("articleID"));
                article.setTitle(rs.getString("title"));
                article.setContent(rs.getString("content"));
                article.setCreateTime(rs.getString("createTime"));
                article.setType(rs.getString("type"));
                articles.add(article);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }

    public Result batchDeleteArticles(List<Integer> articleIDs) {
        // Implement batch delete logic here
        return null;
    }
}
