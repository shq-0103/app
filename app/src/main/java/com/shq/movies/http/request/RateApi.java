package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class RateApi implements IRequestApi {
    private Float rate;
    private Long movieId;
    private String contents;

    @Override
    public String getApi() {
        return "rate";
    }

    public Float getRate() {
        return rate;
    }

    public RateApi setRate(Float rate) {
        this.rate = rate;
        return this;
    }

    public Long getMovieId() {
        return movieId;
    }

    public RateApi setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public RateApi setContents(String contents) {
        this.contents = contents;
        return this;
    }
}