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
    // Query to insert all articles
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllArticles(List<Article> articleList);

    // Query to delete all articles
    @Query("DELETE FROM articles")
    void deleteAllArticles();

    // Query to select all articles
    @Query("SELECT * FROM articles ORDER BY article_id")
    List<Article> getAllArticles();
}