package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;
public final class ReviewCommentApi implements IRequestApi {

    private long id;
    private int page;
    private int pageSize;

    @Override
    public String getApi() {
        return "comment/"+id;
    }

    public long getId() {
        return id;
    }

    public ReviewCommentApi setId(long id) {
        this.id = id;
        return this;
    }

    public int getPage() {
        return page;
    }

    public ReviewCommentApi setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ReviewCommentApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}