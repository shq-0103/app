package com.shq.movies.http.server;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/12/07
 *    desc   : 测试环境
 */
public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return "http://192.168.31.115:7001/";
    }

    @Override
    public String getPath() {
        return "";
    }
}