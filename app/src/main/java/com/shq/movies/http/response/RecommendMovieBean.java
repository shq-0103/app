package com.shq.movies.http.response;

import java.util.List;

public final class RecommendMovieBean {
    private Long movieId;
    private List<MovieBean> list;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public List<MovieBean> getList() {
        return list;
    }

    public void setList(List<MovieBean> list) {
        this.list = list;
    }
}