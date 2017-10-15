/*
Salman Mujtaba: 800969897
Sharan Girdhani: 800960333
MusicTrack
 */
package com.example.sharangirdhani.homework05;

import java.io.Serializable;

/**
 * Created by sharangirdhani on 10/8/17.
 */
public class MusicTrack implements Serializable {
    private String name;
    private String artist;
    private String url;
    private String imgSmall;
    private String imgLarge;
    private boolean isFavorite;

//    public boolean isFavorite() {
//        return isFavorite;
//    }

//    public void setFavorite(boolean favorite) {
//        isFavorite = favorite;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgLarge() {
        return imgLarge;
    }

    public void setImgLarge(String imgLarge) {
        this.imgLarge = imgLarge;
    }
//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicTrack that = (MusicTrack) o;
        if (!name.equals(that.name)) return false;
        if (!artist.equals(that.artist)) return false;
        if (!url.equals(that.url)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "MusicTrack{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", imgSmall='" + imgSmall + '\'' +
                ", imgLarge='" + imgLarge + '\'' +
                '}';
    }
}