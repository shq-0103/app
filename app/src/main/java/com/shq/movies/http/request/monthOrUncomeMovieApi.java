package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class monthOrUncomeMovieApi implements IRequestApi {
    private int page;
    private int pageSize;
    private String order;

    @Override
    public String getApi() {
        return "movies/this/month";
    }
    public int getPage() {
        return page;
    }

    public monthOrUncomeMovieApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public monthOrUncomeMovieApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public monthOrUncomeMovieApi setOrder(String order) {
        this.order = order;
        return this;
    }
}