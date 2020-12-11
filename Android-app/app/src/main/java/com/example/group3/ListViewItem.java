package com.example.group3;

import android.graphics.Bitmap;

class ListViewItem {
    String subtitle;
    Bitmap imgid;
    String usernameArrayList;
    String latitude;
    String longitude;
    String timestamp;

    public ListViewItem(String sub, Bitmap i, String username, String lat, String lng, String time) {
        this.subtitle = sub;
        this.imgid = i;
        this.usernameArrayList = username;
        this.latitude = lat;
        this.longitude = lng;
        this.timestamp = time;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap imgid) {
        this.imgid = imgid;
    }

    public String getUsernameArrayList() {
        return usernameArrayList;
    }

    public void setUsernameArrayList(String usernameArrayList) {
        this.usernameArrayList = usernameArrayList;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
