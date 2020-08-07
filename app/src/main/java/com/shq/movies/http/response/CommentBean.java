package com.shq.movies.http.response;

public class CommentBean {
    private long id;
    private String contents;
    private long useId;
    private long toId;
    private long type;
    private long replyId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getuserId() {
        return useId;
    }

    public void setuserId(long useId) {
        this.useId = useId;
    }

    public long gettoId() {
        return toId;
    }

    public void settoId(long toId) {
        this.toId = toId;
    }
    public long getreplyId() {
        return replyId;
    }

    public void setreplyId(long replyId) {
        this.replyId = replyId;
    }

}
