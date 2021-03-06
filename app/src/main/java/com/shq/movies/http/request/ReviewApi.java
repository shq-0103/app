package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class ReviewApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movieReview";
    }

    private String order;
    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ReviewApi setPage(int page) {
        this.page = page;
        return this;
    }

    public ReviewApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public ReviewApi setOrder(String order) {
        this.order = order;
        return this;
    }
}