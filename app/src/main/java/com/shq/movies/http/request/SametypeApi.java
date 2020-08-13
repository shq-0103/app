package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class SametypeApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movies/like/this";
    }

    private int page;
    private int pageSize;


    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public SametypeApi setPage(int page) {
        this.page = page;
        return this;
    }

    public SametypeApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}