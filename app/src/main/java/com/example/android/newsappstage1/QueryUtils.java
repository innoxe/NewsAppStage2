package com.example.android.newsappstage1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryUtils {

    /**
     * Constant for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    /**
     * Send query to Guardian API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Ready to extract relevant fields from the JSON response API and create a list of {@link News}
        List<News> newsList = extractNews(jsonResponse);

        // Return the list of news
        return newsList;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<News> extractNews(String newsJSON) {

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> newsList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject jsonObject = new JSONObject(newsJSON);

            JSONArray newsResults = jsonObject.getJSONObject("response").getJSONArray("results");

            for(int i = 0; i < newsResults.length(); i++) {
                JSONObject currentResult = newsResults.getJSONObject(i);

                //JSONObject properties = currentResult.getJSONObject("properties");

                // Extract the kewy value "sectionName"
                String sectionName = currentResult.getString("sectionName");

                // Extract the key value "webTitle". This will be the headline
                String headline = currentResult.getString("webTitle");

                // Extract the key value "webURL". The webpage of article.
                String webUrl = currentResult.getString("webUrl");

                // Extract and parse default pattern of the key value "webPublicationDate" with date and hour of publication
                String webPublicationDate = currentResult.getString("webPublicationDate");
                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());

                Date dateNews = null;
                try {
                    dateNews = parser.parse(webPublicationDate);
                } catch (ParseException e) {

                    Log.e(LOG_TAG, "Problem parsing the news date", e);

                }

                // Grab "Fields" element with other request pieces of information
                JSONObject currentField = currentResult.getJSONObject("fields");

                // Extract the value of fields "trailText". A lead paragraph.
                String trailText = currentField.getString("trailText");

                // Extract the value of field "byline". Fullname Author of news
                //String byline = currentField.getString("byline");
                String byline = !currentField.isNull("byline") ? currentField.getString("byline") : "";


                // Extract the value of "thumbanil"
                String thumbnail = currentField.getString("thumbnail");

                // Create a new {@link News} object with the all key.
                // and url from the JSON response.
                News newsObj = new News(headline, trailText, byline, sectionName, dateNews, webUrl, thumbnail );

                // Add the new {@link News} to the list of newsList.
                newsList.add(newsObj);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return newsList;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
