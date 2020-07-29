package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class DeleteCollectApi implements IRequestApi {
    private long favoriteId;
    private int type;

    @Override
    public String getApi() {
        return "delete/collect";
    }

    public long getId() {
        return favoriteId;
    }

    public DeleteCollectApi setId(long favoriteId) {
        this.favoriteId = favoriteId;
        return this;
    }

    public int getType() {
        return type;
    }

    public DeleteCollectApi setType(int type) {
        this.type = type;
        return this;
    }
}
