package com.example.movietitledisplay.model;

import java.io.Serializable;

// Represents a single movie result or detail
public class Movie implements Serializable {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    // Additional fields used in MovieDetailsActivity
    private String Rated;
    private String Runtime;
    private String Genre;
    private String Plot;

    // Getters for base fields
    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }

    // Getters for details view
    public String getRated() {
        return Rated;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public String getPlot() {
        return Plot;
    }
}
