package com.example.xyzreader.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Class to deal with url's and retrieving data from the internet
 */
final public class ArticleNetworkUtilities {
    // Declare constants
    private static final String ARTICLE_URL =
            "https://raw.githubusercontent.com/SuperAwesomeness/XYZReader/master/data.json";
    private static final String ORIGINAL_ARTICLE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58c5d68f_xyz-reader/xyz-reader.json";


    /**
     * Default constructor
     * Builds the URL used to retrieve the data
     */
    public ArticleNetworkUtilities() {
    }

    /**
     * Method to get the response from the built URL
     * @return the response
     * @throws IOException in case of error
     */
    public static String getResponseFromHttpUrl() throws IOException {
        // Build Uri
        Uri builtUri = Uri.parse(ORIGINAL_ARTICLE_URL).buildUpon().build();
        URL url = null;
        // Convert to URL
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) {
            return null;
        }
        // Declare variable for the connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            // Create an input stream
            InputStream in = urlConnection.getInputStream();

            // Use scanner to get response
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            // Test for response
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                // get response
                return scanner.next();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // in case of error return null
            return null;
        } finally {
            // disconnect stream
            urlConnection.disconnect();
        }
    }
}
