package com.example.flierdance_steminist;

public class Video {
    private String videoUri, caption, user;

    public Video() {

    }

    public Video(String videoUri, String caption, String user){
        this.videoUri = videoUri;
        this.caption = caption;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getCaption() {
        return caption;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }


}
