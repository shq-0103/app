package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class UserInfoApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user";
    }
}