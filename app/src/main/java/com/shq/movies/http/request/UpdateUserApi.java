package com.shq.movies.http.request;

import com.hjq.http.config.IRequestApi;

public final class UpdateUserApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/info";
    }

    private String gender;
    private long birthday;
    private String nickname;
    private String email;
    private String avatar;
    private long tags;
    private String introduction;

    public String getGender() {
        return gender;
    }

    public UpdateUserApi setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public long getBirthday() {
        return birthday;
    }

    public UpdateUserApi setBirthday(long birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UpdateUserApi setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUserApi setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UpdateUserApi setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public long getTags() {
        return tags;
    }

    public UpdateUserApi setTags(long tags) {
        this.tags = tags;
        return this;
    }

    public String getIntroduction() {
        return introduction;
    }

    public UpdateUserApi setIntroduction(String introduction) {
        this.introduction = introduction;
        return this;
    }


}
