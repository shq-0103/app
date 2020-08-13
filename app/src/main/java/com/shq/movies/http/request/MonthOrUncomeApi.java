package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;


public final class MonthOrUncomeApi implements IRequestApi {
    // set type "uncoming" get uncoming movie ,not set get this month movie
    private String type;
    private int page;
    private int pageSize;
    @Override
    public String getApi() {
        return "movies/this/month";
    }

    public String getType() {
        return type;
    }

    public MonthOrUncomeApi setType(String type) {
        this.type = type;
        return this;
    }

    public int getPage() {
        return page;
    }

    public MonthOrUncomeApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public MonthOrUncomeApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}