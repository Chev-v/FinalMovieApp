package com.example.movietitledisplay.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movietitledisplay.R;
import com.example.movietitledisplay.model.Movie;
import com.example.movietitledisplay.network.OmdbApiService;
import com.example.movietitledisplay.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Shows detailed information for a single movie
public class MovieDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, ratedTextView, runtimeTextView, genreTextView, plotTextView;
    private ImageView imageViewPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Link views
        titleTextView = findViewById(R.id.textViewTitle);
        ratedTextView = findViewById(R.id.textViewRated);
        runtimeTextView = findViewById(R.id.textViewRuntime);
        genreTextView = findViewById(R.id.textViewGenre);
        plotTextView = findViewById(R.id.textViewPlot);
        imageViewPoster = findViewById(R.id.imageViewPoster);

        // Get Movie passed from MainActivity
        Movie basicMovie = (Movie) getIntent().getSerializableExtra("movie");

        if (basicMovie != null && basicMovie.getImdbID() != null) {
            fetchFullMovieDetails(basicMovie.getImdbID());
        } else {
            Toast.makeText(this, "Movie data unavailable", Toast.LENGTH_SHORT).show();
            finish(); // Exit screen if no data
        }
    }

    // Fetches full movie info using IMDB ID
    private void fetchFullMovieDetails(String imdbID) {
        OmdbApiService apiService = RetrofitClient.getRetrofitInstance().create(OmdbApiService.class);
        Call<Movie> call = apiService.getMovieDetails("873eb301", imdbID);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();
                    titleTextView.setText(movie.getTitle());
                    ratedTextView.setText("Rated: " + movie.getRated());
                    runtimeTextView.setText("Runtime: " + movie.getRuntime());
                    genreTextView.setText("Genre: " + movie.getGenre());
                    plotTextView.setText("Plot: " + movie.getPlot());

                    // Load image into the poster ImageView using Picasso
                    if (movie.getPoster() != null && !movie.getPoster().equals("N/A")) {
                        Picasso.get().load(movie.getPoster()).into(imageViewPoster);
                    } else {
                        imageViewPoster.setImageResource(R.drawable.ic_launcher_background);
                    }
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
