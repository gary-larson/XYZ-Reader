package com.example.xyzreader.utilities;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Class to deal with Executor
 */
public class ArticleExecutor implements Executor {

    /**
     * Method to start a thread to execute
     * @param r runnable to start
     */
    public void execute(@NonNull Runnable r) {
        new Thread(r).start();
    }
}
