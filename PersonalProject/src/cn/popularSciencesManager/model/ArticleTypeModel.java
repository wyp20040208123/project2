package cn.popularSciencesManager.model;

import javax.swing.*;
import java.awt.*;

public class ArticleTypeModel implements AllModel {
    String type;
    static String databaseName="articletype";
    static String[] primaryKey={"type"};

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
