package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;
public final class AddCommentApi implements IRequestApi {

    private String contents;
    private Long toId;
    private Long replyId;
    private int type;

    @Override
    public String getApi() {
        return "comment";
    }

    public String getContents() {
        return contents;
    }

    public AddCommentApi setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Long getToId() {
        return toId;
    }

    public AddCommentApi setToId(Long toId) {
        this.toId = toId;
        return this;
    }

    public Long getReplyId() {
        return replyId;
    }

    public AddCommentApi setReplyId(Long replyId) {
        this.replyId = replyId;
        return this;
    }

    public int getType() {
        return type;
    }

    public AddCommentApi setType(int type) {
        this.type = type;
        return this;
    }
}
