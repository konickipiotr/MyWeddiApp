package com.myweddi.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myweddi.model.Photo;
import com.myweddi.model.User;
import com.myweddi.model.post.Post;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostView {
    private Long id;
    private Long weddingid;
    private Long userid;
    //private LocalDateTime creationdate;
    private String description;

    private String username;
    private String userphoto;

    private String postdate;
    private String posttime;

    private boolean isLiked;
    private int likeNumber;

    private List<Photo> photos = new ArrayList<>();
    private List<CommentView> comments = new ArrayList<>();

    public PostView() {
    }

    public PostView(Post p, User user) {
        this.id = p.getId();
        this.weddingid = p.getWeddingid();
        this.userid = p.getUserid();
        //this.creationdate = p.getCreationdate();
        this.description = p.getDescription();
        this.username = user.getName();
        this.userphoto = user.getPhoto();
    }

//    public void covert() {
//        this.postdate = this.creationdate.toLocalDate().toString();
//        this.posttime = this.creationdate.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

//    public LocalDateTime getCreationdate() {
//        return creationdate;
//    }
//
//    public void setCreationdate(LocalDateTime creationdate) {
//        this.creationdate = creationdate;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<CommentView> getComments() {
        return comments;
    }

    public void setComments(List<CommentView> comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }
}
