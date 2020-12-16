package ru.zigzag55.mymovies1;

import androidx.appcompat.app.AppCompatActivity;
import ru.zigzag55.mymovies1.data.Movie;
import ru.zigzag55.mymovies1.utils.JSONUtils;
import ru.zigzag55.mymovies1.utils.NetworkUtils;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView1);

        //getting JSON from internet
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY, 3);

        //getting array of movies
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        if (jsonObject == null) {
            Toast.makeText(this, "Null json", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Good json. Elements: " + movies.size(), Toast.LENGTH_LONG).show();
        }

        StringBuilder builder = new StringBuilder();
        for (Movie movie: movies) {
            builder.append(movie.getTitle()).append("\n");
        }
        Log.i("MyResult", builder.toString());
        textView.setText(builder.toString());
    }
}