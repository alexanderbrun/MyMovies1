package ru.zigzag55.mymovies1.utils;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.zigzag55.mymovies1.R;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";

    private static final String API_KEY = Key.API_KEY;
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";

    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        JSONObject result = null;
        URL url = buildURL(sortBy, page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static URL buildURL(int sortBy, int page) {
        //Create query URL
        URL result = null;
        String methodOfSort;
        if (sortBy == POPULARITY) {
            methodOfSort = SORT_BY_POPULARITY;
        } else methodOfSort = SORT_BY_TOP_RATED;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        //Read and adaptation JSON response from site

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 00) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
