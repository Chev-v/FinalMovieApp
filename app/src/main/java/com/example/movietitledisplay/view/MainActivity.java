package com.example.movietitledisplay.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietitledisplay.R;
import com.example.movietitledisplay.adapter.MovieAdapter;
import com.example.movietitledisplay.model.Movie;
import com.example.movietitledisplay.model.MovieSearchResponse;
import com.example.movietitledisplay.network.OmdbApiService;
import com.example.movietitledisplay.network.RetrofitClient;
import com.example.movietitledisplay.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Main screen where user can search for movies
public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Button buttonSearch;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link views
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);

        // Setup RecyclerView
        movieAdapter = new MovieAdapter(movieList, this);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMovies.setAdapter(movieAdapter);

        // Search button click
        buttonSearch.setOnClickListener(v -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchMovies(query);
            } else {
                Toast.makeText(MainActivity.this, "Enter a search term", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Makes API call to fetch movie search results
    private void fetchMovies(String query) {
        progressBar.setVisibility(View.VISIBLE);

        OmdbApiService apiService = RetrofitClient.getRetrofitInstance().create(OmdbApiService.class);
        Call<MovieSearchResponse> call = apiService.searchMovies(Constants.API_KEY, query);

        call.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getSearch() != null) {
                    movieList.clear();
                    movieList.addAll(response.body().getSearch());
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
