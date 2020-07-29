package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class CollectMovieIdListApi implements IRequestApi {

    @Override
    public String getApi() {
        return "collect/movie/id";
    }
}
