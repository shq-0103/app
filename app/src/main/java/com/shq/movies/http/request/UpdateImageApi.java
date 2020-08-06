package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

import java.io.File;

public final class UpdateImageApi implements IRequestApi {

    @Override
    public String getApi() {
        return "file";
    }

    /** 图片文件 */
    private File image;
    private int type=1;

    public UpdateImageApi setImage(File image) {
        this.image = image;
        return this;
    }
}