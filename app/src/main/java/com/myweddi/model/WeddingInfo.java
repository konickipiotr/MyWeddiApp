package com.myweddi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeddingInfo {

    private long weddingid;
    private String name;
    private double longitude;
    private double latitude;
    private String address;
    private String realPath;
    private String webAppPath;

    public WeddingInfo() {
    }

    public WeddingInfo(long weddingid) {
        this.weddingid = weddingid;
    }

    public long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(long weddingid) {
        this.weddingid = weddingid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }
}
