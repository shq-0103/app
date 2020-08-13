package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class MovieLikeThisApi implements IRequestApi {
    private long movieId;
    private int num;

    @Override
    public String getApi() {
        return "movies/like/this";
    }

    public long getMovieId() {
        return movieId;
    }

    public MovieLikeThisApi setMovieId(long movieId) {
        this.movieId = movieId;
        return this;
    }

    public int getNum() {
        return num;
    }

    public MovieLikeThisApi setNum(int num) {
        this.num = num;
        return this;
    }
}