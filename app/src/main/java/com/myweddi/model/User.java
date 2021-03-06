package com.myweddi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private String photo;
    private String role;

    public User(Host host) {
        this.id = host.getId();
        this.firstname = host.getFirstname();
        this.lastname = host.getLastname();
        this.photo = host.getPhoto();
        this.role = host.getRole();
    }

    public User(Guest guest) {
        this.id = guest.getId();
        this.firstname = guest.getFirstname();
        this.lastname = guest.getLastname();
        this.photo = guest.getPhoto();
        this.role = guest.getRole();
    }

    public String getName(){
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", photo='" + photo + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

