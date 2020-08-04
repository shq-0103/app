package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class QueryMovieApi implements IRequestApi {

    private int page;
    private int pageSize;
    private String name;
    private String order;
    private String genres;
    private String decade;

    @Override
    public String getApi() {
        return "movies";
    }

    public int getPage() {
        return page;
    }

    public QueryMovieApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public QueryMovieApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getName() {
        return name;
    }

    public QueryMovieApi setName(String name) {
        this.name = name;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public QueryMovieApi setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getGenres() {
        return genres;
    }

    public QueryMovieApi setGenres(String genres) {
        this.genres = genres;
        return this;
    }

    public String getDecade() {
        return decade;
    }

    public QueryMovieApi setDecade(String decade) {
        this.decade = decade;
        return this;
    }
}