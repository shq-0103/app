package com.shq.movies.http.response;


public final class MovieBean {

  private long id;
  private String name;
  private String writer;
  private String actor;
  private String director;
  private String cover;
  private String genres;
  private String mins;
  private String releaseDate;
  private double score;
  private String intro;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }


  public String getActor() {
    return actor;
  }

  public void setActor(String actor) {
    this.actor = actor;
  }


  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }


  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }


  public String getGenres() {
    return genres;
  }

  public void setGenres(String genres) {
    this.genres = genres;
  }


  public String getMins() {
    return mins;
  }

  public void setMins(String mins) {
    this.mins = mins;
  }


  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }


  public int getScore() {
    return (int) score;
  }

  public void setScore(double score) {
    this.score = score;
  }


  public String getIntro() {
    return intro;
  }

  public void setIntro(String intro) {
    this.intro = intro;
  }

}
