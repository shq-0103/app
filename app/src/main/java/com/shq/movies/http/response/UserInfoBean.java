package com.shq.movies.http.response;

public final class UserInfoBean {

    private long id;
    private String username;
    private String gender;
    private long birthday;
    private String nickname;
    private String email;
    private String avatar;
    private long tags;
    private long status;
    private String usercol;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public long getTags() {
        return tags;
    }

    public void setTags(long tags) {
        this.tags = tags;
    }


    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }


    public String getUsercol() {
        return usercol;
    }

    public void setUsercol(String usercol) {
        this.usercol = usercol;
    }
}