package com.myweddi.module.weddinginfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeddingInfo {

    private Long weddingid;
    private String churchname;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime ceremenytime;
    private double chLongitude;
    private double chLatitude;
    private String churchaddress;
    private String info;
    private String chRealPath;
    private String chWebAppPath;

    private String weddinghousename;
    private double wLongitude;
    private double wLatitude;
    private String wAddress;
    private String wRealPath;
    private String wWebAppPath;

    public void update(WeddingInfo weddingInfo) {
        this.churchname = weddingInfo.getChurchname();
        this.ceremenytime = weddingInfo.getCeremenytime();
        this.chLongitude = weddingInfo.getChLongitude();
        this.chLatitude = weddingInfo.getChLatitude();
        this.churchaddress = weddingInfo.getChurchaddress();
        this.info = weddingInfo.getInfo();

        this.weddinghousename = weddingInfo.getWeddinghousename();
        this.wLongitude = weddingInfo.getwLongitude();
        this.wLatitude = weddingInfo.getwLatitude();
        this.wAddress = weddingInfo.getwAddress();
    }

    public WeddingInfo(Long weddingid) {
        this.weddingid = weddingid;
    }

    public WeddingInfo() {
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getChurchname() {
        return churchname;
    }

    public void setChurchname(String churchname) {
        this.churchname = churchname;
    }

    public LocalDateTime getCeremenytime() {
        return ceremenytime;
    }

    public void setCeremenytime(LocalDateTime ceremenytime) {
        this.ceremenytime = ceremenytime;
    }

    public double getChLongitude() {
        return chLongitude;
    }

    public void setChLongitude(double chLongitude) {
        this.chLongitude = chLongitude;
    }

    public double getChLatitude() {
        return chLatitude;
    }

    public void setChLatitude(double chLatitude) {
        this.chLatitude = chLatitude;
    }

    public String getChurchaddress() {
        return churchaddress;
    }

    public void setChurchaddress(String churchaddress) {
        this.churchaddress = churchaddress;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getChRealPath() {
        return chRealPath;
    }

    public void setChRealPath(String chRealPath) {
        this.chRealPath = chRealPath;
    }

    public String getChWebAppPath() {
        return chWebAppPath;
    }

    public void setChWebAppPath(String chWebAppPath) {
        this.chWebAppPath = chWebAppPath;
    }

    public String getWeddinghousename() {
        return weddinghousename;
    }

    public void setWeddinghousename(String weddinghousename) {
        this.weddinghousename = weddinghousename;
    }

    public double getwLongitude() {
        return wLongitude;
    }

    public void setwLongitude(double wLongitude) {
        this.wLongitude = wLongitude;
    }

    public double getwLatitude() {
        return wLatitude;
    }

    public void setwLatitude(double wLatitude) {
        this.wLatitude = wLatitude;
    }

    public String getwAddress() {
        return wAddress;
    }

    public void setwAddress(String wAddress) {
        this.wAddress = wAddress;
    }

    public String getwRealPath() {
        return wRealPath;
    }

    public void setwRealPath(String wRealPath) {
        this.wRealPath = wRealPath;
    }

    public String getwWebAppPath() {
        return wWebAppPath;
    }

    public void setwWebAppPath(String wWebAppPath) {
        this.wWebAppPath = wWebAppPath;
    }
}
