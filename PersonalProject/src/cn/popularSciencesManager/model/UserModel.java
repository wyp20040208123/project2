package cn.popularSciencesManager.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel implements AllModel {
    int userID;
    String userName;
    String createData;
    String password;
    String email;
    String userType;
    static String databaseName="user";
    static String[] primaryKey={"userID"};

    public  void setInfoFromResult(ResultSet rs) {
        try{
            userID=rs.getInt("userID");
            userName=rs.getString("userName");
            email=rs.getString("email");
            createData=rs.getString("createData");
            userType=rs.getString("userType");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateData() {
        return createData;
    }

    public void setCreateData(String createData) {
        this.createData = createData;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String[] getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
