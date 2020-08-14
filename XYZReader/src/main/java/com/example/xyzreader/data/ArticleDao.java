package com.example.xyzreader.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object class for the Article class
 */
@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticle(Article article);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllArticles(List<Article> articleList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateArticle(Article Article);

    @Delete
    void deleteAllArticles(List<Article> articleList);

    @Query("SELECT * FROM articles")
    List<Article> getAllArticles();

    @Query("SELECT * FROM articles WHERE article_id = :articleId")
    Article getArticle(int articleId);
}