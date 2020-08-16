package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class DeleteReviewApi implements IRequestApi {

    private long id;
    @Override
    public String getApi() {
        return "movieReview/delete";
    }

    public long getId() {
        return id;
    }

    public DeleteReviewApi setId(long id) {
        this.id = id;
        return this;
    }
}