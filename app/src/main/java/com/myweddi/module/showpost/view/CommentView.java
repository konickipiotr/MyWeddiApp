package com.myweddi.module.showpost.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myweddi.model.User;
import com.myweddi.module.showpost.model.Comment;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentView{

    private Long id;
    private Long postid;
    private Long userid;
    private String content;
    private String username;
    private String userphoto;
    private String postdatetime;
    private boolean myComment;

    public CommentView() {
    }

    public CommentView(Comment c, User user) {
        this.id = c.getId();
        this.postid = c.getPostid();
        this.userid = c.getUserid();
        this.content = c.getContent();
        this.username = user.getName();
        this.userphoto = user.getWebAppPath();
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    public boolean isMyComment() {
        return myComment;
    }

    public void setMyComment(boolean myComment) {
        this.myComment = myComment;
    }
}
