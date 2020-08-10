package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class OnLikeApi implements IRequestApi {

    private long toId;
    private int type;
    @Override
    public String getApi() {
        return "like";
    }

    public long getToId() {
        return toId;
    }

    public OnLikeApi setToId(long toId) {
        this.toId = toId;
        return this;
    }

    public int getType() {
        return type;
    }

    public OnLikeApi setType(int type) {
        this.type = type;
        return this;
    }
}