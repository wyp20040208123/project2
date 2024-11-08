package cn.popularSciencesManager.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticlesModel implements AllModel {
    int articleID;
    int authorID;
    String title;
    String content;
    String createTime;
    String type;
    static String databaseName="articles";
    static String[] primaryKey={"articleID"};

    public void setInfoFromResult(ResultSet rs) throws SQLException {
        articleID=rs.getInt("articleID");
        authorID=rs.getInt("authorID");
        title=rs.getString("title");
        content=rs.getString("content");
        createTime=rs.getString("createTime");
        type=rs.getString("type");
    }


    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
