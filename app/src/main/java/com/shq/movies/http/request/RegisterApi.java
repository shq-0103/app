package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 用户注册
 */
public final class RegisterApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/signin";
    }

    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;

    public RegisterApi setPhone(String username) {
        this.username = username;
        return this;
    }

    public RegisterApi setPassword(String password) {
        this.password = password;
        return this;
    }
}