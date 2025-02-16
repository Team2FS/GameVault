package com.example.gamevault.ui.social;

import java.util.Date;

public class Post {
    private String postId;
    private String userId;
    private String username;
    private String content;
    private String imageUrl;
    private String profilePictureUrl;
    private Date timestamp;
    private int likes;

    public Post() {}

    // constructor
    public Post(String postId, String userId, String username, String content, String imageUrl, String profilePictureUrl, Date timestamp, int likes) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.imageUrl = imageUrl;
        this.profilePictureUrl = profilePictureUrl;
        this.timestamp = timestamp;
        this.likes = likes;
    }

    // Getters and setters
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}