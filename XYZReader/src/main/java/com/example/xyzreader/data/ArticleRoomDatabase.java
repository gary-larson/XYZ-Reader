package com.example.xyzreader.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to deal with Article database
 */
@Database(entities = {Article.class}, version = 2)
public abstract class ArticleRoomDatabase extends RoomDatabase {
    // declare dao class
    public abstract ArticleDao articleDao();

    //setup database and threads
    private static volatile ArticleRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gett for database
     * @param context to use database
     * @return database
     */
    public static ArticleRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ArticleRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleRoomDatabase.class, "article_database")
                            .fallbackToDestructiveMigrationFrom(1)
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}

