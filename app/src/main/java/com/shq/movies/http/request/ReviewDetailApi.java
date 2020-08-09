package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class ReviewDetailApi implements IRequestApi {

    private Long reviewId;
    @Override
    public String getApi() {
        return "movieReview/"+this.reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public ReviewDetailApi setReviewId(Long reviewId) {
        this.reviewId = reviewId;
        return this;
    }
}