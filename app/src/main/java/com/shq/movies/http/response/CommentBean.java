package com.shq.movies.http.response;

public class CommentBean {
    private long id;
    private String contents;
    private long useId;
    private long toId;
    private long type;
    private long replyId;
    private long time;
    private String nickname;
    private String avatar;


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

    public long getUseId() {
        return useId;
    }

    public void setUseId(long useId) {
        this.useId = useId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
