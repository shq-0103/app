package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class SeenApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movies/seen";
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

    public SeenApi setPage(int page) {
        this.page = page;
        return this;
    }

    public SeenApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}