package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class MyReviewApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movieReview/my";
    }

    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public MyReviewApi setPage(int page) {
        this.page = page;
        return this;
    }

    public MyReviewApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}