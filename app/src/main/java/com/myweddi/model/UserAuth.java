package com.myweddi.model;

import com.myweddi.enums.UserStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class UserAuth {
    private Long id;
    private String username;
    private String password;
    private String role;
    private UserStatus status;

    public UserAuth() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
