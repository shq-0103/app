package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class CollectMovieApi implements IRequestApi {

    @Override
    public String getApi() {
        return "collect/movies";
    }

    private int page;
    private int pageSize;
    private String order;
    private String genres;
    private String decade;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public CollectMovieApi setPage(int page) {
        this.page = page;
        return this;
    }

    public CollectMovieApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public CollectMovieApi setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getGenres() {
        return genres;
    }

    public CollectMovieApi setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public String getDecade() {
        return decade;
    }

    public CollectMovieApi setDecade(String decade) {
        this.decade = decade;
        return this;
    }
}