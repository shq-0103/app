package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class CommentApi implements IRequestApi {

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

    public CommentApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public CommentApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
    public Long getMovieId() {
        return movieId;
    }

    public CommentApi setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }
    }
