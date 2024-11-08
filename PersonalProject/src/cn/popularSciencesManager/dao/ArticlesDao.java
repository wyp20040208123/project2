package cn.popularSciencesManager.dao;

import cn.popularSciencesManager.model.*;
import com.mysql.cj.xdevapi.Result;

import java.util.List;

public interface ArticlesDao {
    // 单一操作
    int insertArticle(ArticlesModel article);
    int updateArticle(ArticlesModel article);
    int deleteArticle(int articleID);
    ArticlesModel getArticle(int articleID);

    // 集体操作
    List<ArticlesModel> getAllArticles();

}
