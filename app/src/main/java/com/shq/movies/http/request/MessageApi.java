package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class MessageApi implements IRequestApi {
    public String getApi() {
        return "notice/my";
    }

    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public MessageApi setPage(int page) {
        this.page = page;
        return this;
    }

    public MessageApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}

