package com.rakeshSingh.bulletin.database;

import com.rakeshSingh.bulletin.models.Article;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ArticlesDAO {

    @Query("Select * from articles")
    List<Article> getAllArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);

    @Query("Select url from articles where url is :url")
    String getArticleURL(String url);

    @Delete
    void deleteArticle(Article article);

}
