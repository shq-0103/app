package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class HistoryApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movies/history";
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

    public HistoryApi setPage(int page) {
        this.page = page;
        return this;
    }

    public HistoryApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}