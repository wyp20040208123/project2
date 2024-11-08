package cn.popularSciencesManager.model;

public class ReviewModel implements AllModel {
    int articleID;
    int userID;
    String review;
    String timestamp;
    int likeNum;
    static String databaseName="review";
    static String[] primaryKey={"articleID","createTime"};

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String[] getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
