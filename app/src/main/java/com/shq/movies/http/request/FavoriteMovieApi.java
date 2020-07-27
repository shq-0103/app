package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;
import com.shq.movies.http.model.PageQuery;


public final class FavoriteMovieApi implements IRequestApi {

    @Override
    public String getApi() {
        return "favorite/movies";
    }

    private int page;
    private int pageSize;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public FavoriteMovieApi setPage(int page) {
        this.page = page;
        return this;
    }

    public FavoriteMovieApi setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}