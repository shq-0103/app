package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class RecommendByIdApi implements IRequestApi {

    private long movieId;
    private int num;

    @Override
    public String getApi() {
        return "movies/recommend/" + movieId;
    }

    public long getMovieId() {
        return movieId;
    }

    public RecommendByIdApi setMovieId(long movieId) {
        this.movieId = movieId;
        return this;
    }

    public int getNum() {
        return num;
    }

    public RecommendByIdApi setNum(int num) {
        this.num = num;
        return this;
    }
}