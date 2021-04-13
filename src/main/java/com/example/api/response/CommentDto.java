package com.example.api.response;

import com.example.model.PostComment;

public class CommentDto implements Comparable<CommentDto>{
    private int id;
    private long timestamp;
    private String text;
    private UserDTO user;

    public CommentDto(PostComment postComment) {
        this.id = postComment.getId();
        this.timestamp = Long.parseLong(String.valueOf(postComment.getDate().getTime()).substring(0,10));
        this.text = postComment.getText();
        this.user = new UserDTO(postComment.getUser().getId(), postComment.getUser().getName(), postComment.getUser().getPhoto());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public int compareTo(CommentDto o) {
        return (int) (this.timestamp - o.timestamp);
    }
}
