package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class MovieDetailApi implements IRequestApi {
    private Long id;

    @Override
    public String getApi() {
        return "movies/"+this.id;
    }

    public Long getId() {
        return id;
    }

    public MovieDetailApi setId(Long id) {
        this.id = id;
        return this;
    }
}