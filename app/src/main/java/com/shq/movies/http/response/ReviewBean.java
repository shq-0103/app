package com.shq.movies.http.response;

public final class ReviewBean {
    private long id;
    private String title;
    private String contents;
    private long useId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


}
