package com.shq.movies.event;

public final class PickMovieEvent {
    public long movieId;

    public PickMovieEvent(long movieId){
        this.movieId = movieId;
    }
}