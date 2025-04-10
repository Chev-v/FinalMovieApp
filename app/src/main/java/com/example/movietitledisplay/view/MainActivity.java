package com.example.movietitledisplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movietitledisplay.R;
import com.example.movietitledisplay.adapter.MovieAdapter;
import com.example.movietitledisplay.model.Movie;
import com.example.movietitledisplay.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

// Main screen that shows movie search UI
public class MainActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private EditText searchInput;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If user is not signed in, redirect to login screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Set up ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Configure RecyclerView for displaying search results
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MovieAdapter(this, movie -> {
            // Go to details when a movie is clicked
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Search input field
        searchInput = findViewById(R.id.searchInput);
        findViewById(R.id.searchButton).setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                movieViewModel.searchMovies(query);
            }
        });

        // Update list when movies load
        movieViewModel.getMovies().observe(this, this::updateMovies);

        // Logout button click event
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    // This method updates the movie list
    private void updateMovies(List<Movie> movies) {
        adapter.setMovies(movies);
    }
}
