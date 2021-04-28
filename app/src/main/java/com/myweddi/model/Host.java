package com.myweddi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {
    private Long id;
    private String bridefirstname;
    private String bridelastname;
    private String brideemail;
    private String bridephone;
    private String groomfirstname;
    private String groomlastname;
    private String groomemail;
    private String groomphone;
    private String firstname;
    private String lastname;
    private String realPath;
    private String webAppPath;

    public Host(){
        this.firstname = "Państow Młodzi";
        this.lastname = "";
        this.webAppPath = "img/user.png";
    }

    public Host(Long id){
        this();
        this.id = id;
    }

    public String getName(){
        return this.firstname + " " + this.lastname;
    }

    public String getBrideName(){
        return bridefirstname + " " + bridelastname;
    }

    public String getGroomName(){
        return groomfirstname + " " + groomlastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBridefirstname() {
        return bridefirstname;
    }

    public void setBridefirstname(String bridefirstname) {
        this.bridefirstname = bridefirstname;
    }

    public String getBridelastname() {
        return bridelastname;
    }

    public void setBridelastname(String bridelastname) {
        this.bridelastname = bridelastname;
    }

    public String getBrideemail() {
        return brideemail;
    }

    public void setBrideemail(String brideemail) {
        this.brideemail = brideemail;
    }

    public String getBridephone() {
        return bridephone;
    }

    public void setBridephone(String bridephone) {
        this.bridephone = bridephone;
    }

    public String getGroomfirstname() {
        return groomfirstname;
    }

    public void setGroomfirstname(String groomfirstname) {
        this.groomfirstname = groomfirstname;
    }

    public String getGroomlastname() {
        return groomlastname;
    }

    public void setGroomlastname(String groomlastname) {
        this.groomlastname = groomlastname;
    }

    public String getGroomemail() {
        return groomemail;
    }

    public void setGroomemail(String groomemail) {
        this.groomemail = groomemail;
    }

    public String getGroomphone() {
        return groomphone;
    }

    public void setGroomphone(String groomphone) {
        this.groomphone = groomphone;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
