package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class RatingApi implements IRequestApi {
    private int rate;
    private Long movieId;
    private String contents;

    @Override
    public String getApi() {
        return "rate/my";
    }

    public int getRate() {
        return rate;
    }

    public RatingApi setRate(int rate) {
        this.rate = rate;
        return this;
    }

    public Long getMovieId() {
        return movieId;
    }

    public RatingApi setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public RatingApi setContents(String contents) {
        this.contents = contents;
        return this;
    }


    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public RatingApi setPage(int page) {
        this.page = page;
        return this;
    }

    public RatingApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}