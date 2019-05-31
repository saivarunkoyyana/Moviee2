package com.example.acer.Moviee2.ModelClass;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favoritesData")
public class EntityData implements Serializable {
    @PrimaryKey
    public int MovieId;
    private String movieName;
    private String movieDesc;
    private String movieRelease;
    private String moviePoster;
    private String backdrop;
    private String movieVoteAvg;

    public int getMovieId() {
        return MovieId;
    }

    public void setMovieId(int movieId) {
        this.MovieId = movieId;

    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieVoteAvg() {
        return movieVoteAvg;
    }

    public void setMovieVoteAvg(String movieVoteAvg) {
        this.movieVoteAvg = movieVoteAvg;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }
}
