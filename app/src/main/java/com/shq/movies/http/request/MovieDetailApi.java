package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class MovieDetailApi implements IRequestApi {
    private int id;

    @Override
    public String getApi() {
        return "movies/"+this.id;
    }

    public int getId() {
        return id;
    }

    public MovieDetailApi setId(int id) {
        this.id = id;
        return this;
    }
}