package com.example.mohamed.mymedeciene.data;

import java.util.List;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:48
 */

public class Post {
    private String id;
    private String userId;
    private String content;
    private String imgUrl;
    private boolean isLiked;
    private List<Comment> comments;



    public Post(String id, String userId, String content, String imgUrl) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public Post() {
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
