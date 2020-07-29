package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class CollectMovieApi implements IRequestApi {

    @Override
    public String getApi() {
        return "collect/movies";
    }

    private int page;
    private int pageSize;

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
}