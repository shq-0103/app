package com.shq.movies.http.response;

import java.util.List;

public final class IdListBean {
    private List<Integer> collect;
    private List<Integer> seen;

    public List<Integer> getCollect() {
        return collect;
    }

    public void setCollect(List<Integer> collect) {
        this.collect = collect;
    }

    public List<Integer> getSeen() {
        return seen;
    }

    public void setSeen(List<Integer> seen) {
        this.seen = seen;
    }
}