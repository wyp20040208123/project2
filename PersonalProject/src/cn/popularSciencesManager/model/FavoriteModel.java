package cn.popularSciencesManager.model;

public class FavoriteModel implements AllModel {
    int userID;
    int articleID;
    static String databaseName="favorite";
    static String[] primaryKey={"userID","articleID"};

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String[] getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
