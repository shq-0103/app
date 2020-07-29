package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class AddCollectApi implements IRequestApi {

    private long favoriteId;

    public long getFavoriteId() {
        return favoriteId;
    }

    public AddCollectApi setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
        return this;
    }

    public int getType() {
        return type;
    }

    public AddCollectApi setType(int type) {
        this.type = type;
        return this;
    }

    private int type;
    private long time=System.currentTimeMillis();

    @Override
    public String getApi() {
        return "collect";
    }


}
