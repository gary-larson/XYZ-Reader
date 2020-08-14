package com.example.xyzreader.utilities;

/**
 * Class to handle results from network requests
 * @param <T>
 */
public abstract class ArticleResult<T> {
    /**
     * Default constructor
     */
    private ArticleResult() {}

    /**
     * Class to transfer success data
     * @param <T> to set
     */
    public final static class Success<T> extends ArticleResult<T> {
        // Declare variables
        public final T data;

        /**
         * Constructor for success
         * @param data to transfer
         */
        public Success(T data) {
            this.data = data;
        }
    }

    /**
     * Class to transfer error information
     * @param <T> to set
     */
    public final static class Error<T> extends ArticleResult<T> {
        // Declare variables
        public final String mErrorMessage;

        /**
         * Constructor for Error
         * @param errorMessage to send
         */
        public Error(String errorMessage) {
            this.mErrorMessage = errorMessage;
        }
    }
}
