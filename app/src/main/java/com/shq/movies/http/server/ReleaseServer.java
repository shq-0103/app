package com.shq.movies.http.server;

import com.hjq.http.config.IRequestServer;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 正式环境
 */
public class ReleaseServer implements IRequestServer {

    // 47.101.132.207:
    @Override
    public String getHost() {
        return "http://192.168.31.115:7001/";
    }

    @Override
    public String getPath() {
        return "";
    }
}