package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class MovieReviewApi implements IRequestApi {

    private String title;
    private String contents;

    private String images;


    @Override
    public String getApi() {
        return "movieReview";
    }

    public String getTitle() {
        return title;
    }

    public MovieReviewApi setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public MovieReviewApi setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public String getImages() {
        return images;
    }

    public MovieReviewApi setImages(String images) {
        this.images = images;
        return this;
    }
}
