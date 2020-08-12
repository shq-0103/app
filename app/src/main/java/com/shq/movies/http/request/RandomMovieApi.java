package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class RandomMovieApi implements IRequestApi {
    private int num;

    public int getNum() {
        return num;
    }

    public RandomMovieApi setNum(int num) {
        this.num = num;
        return this;
    }

    @Override
    public String getApi() {
        return "movies/random";
    }
}
