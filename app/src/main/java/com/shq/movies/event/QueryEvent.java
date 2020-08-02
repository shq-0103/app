package com.shq.movies.event;

public final class QueryEvent {
    public String key;
    public String order;
    public String genres;
    public String decade;

    public QueryEvent(String key,String order,String decade,String genres){
        this.key = key;
        this.order=order;
        this.genres=genres;
        this.decade=decade;
    }
}
