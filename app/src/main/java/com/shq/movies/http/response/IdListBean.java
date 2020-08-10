package com.shq.movies.http.response;

import java.util.List;

public final class IdListBean {
    private List<Integer> collect;
    private List<Integer> seen;
    private List<Integer> likeReview;
    private List<Integer> likeComment;
    private List<Integer> likeRate;
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

    public List<Integer> getLikeReview() {
        return likeReview;
    }

    public void setLikeReview(List<Integer> likeReview) {
        this.likeReview = likeReview;
    }

    public List<Integer> getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(List<Integer> likeComment) {
        this.likeComment = likeComment;
    }

    public List<Integer> getLikeRate() {
        return likeRate;
    }

    public void setLikeRate(List<Integer> likeRate) {
        this.likeRate = likeRate;
    }
}