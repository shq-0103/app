package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class QueryMovieApi implements IRequestApi {

    private int page;
    private int pageSize;
    private String name;

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
}