package com.myweddi.model.post;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long postid;
    private Long userid;
    private String content;
    private LocalDateTime creationdate;

    public Comment(Long postid, Long userid, String content, LocalDateTime creationdate) {
        this.postid = postid;
        this.userid = userid;
        this.content = content;
        this.creationdate = creationdate;
    }

    public Comment(Long postid, Long userid, String content) {
        this.postid = postid;
        this.userid = userid;
        this.content = content;
        this.creationdate = creationdate;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }
}
