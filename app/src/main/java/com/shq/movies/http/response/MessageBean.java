package com.shq.movies.http.response;

public class MessageBean {
    private long id;
    private long type;
    private long status;
    private String content;
    private long toUserId;
    private long time;
    private long toId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }


    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}
