package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class CommentApi implements IRequestApi {

    private int page;
    private int pageSize;

    @Override
    public String getApi() {
        return "comment";
    }

    public int getPage() {
        return page;
    }

    public CommentApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public CommentApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    }
