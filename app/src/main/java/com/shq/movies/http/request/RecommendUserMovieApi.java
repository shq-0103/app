package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class RecommendUserMovieApi implements IRequestApi {

    @Override
    public String getApi() {
        return "movies/recommend/my";
    }
}