package com.shq.movies.http.model;

public class PageQuery {
    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageQuery setPage(int page) {
        this.page = page;
        return this;
    }

    public PageQuery setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
