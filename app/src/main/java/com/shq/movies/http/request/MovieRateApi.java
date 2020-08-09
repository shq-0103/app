package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class MovieRateApi implements IRequestApi {

    private int page;
    private int pageSize;
    private Long movieId;


    @Override
    public String getApi() {
        return "movies/rate";
    }

    public int getPage() {
        return page;
    }

    public MovieRateApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public MovieRateApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
    public Long getMovieId() {
        return movieId;
    }

    public MovieRateApi setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }
    }
