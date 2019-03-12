package com.sai8.githubsearch.model;

public class JobInfo {

    String title;
    String location;
    String url;
    String des;
    String imageurl;

    public JobInfo(String title, String location, String url, String des,String imageurl) {
        this.title = title;
        this.location = location;
        this.url = url;
        this.des = des;
        this.imageurl=imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public String getDes() {
        return des;
    }
}
