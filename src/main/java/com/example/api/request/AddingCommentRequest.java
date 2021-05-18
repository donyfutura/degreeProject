package com.example.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AddingCommentRequest {

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("post_id")
    private String postId;

    private String text;

    public AddingCommentRequest() {
    }

    public AddingCommentRequest(String parentId, String postId, String text) {
        this.parentId = parentId;
        this.postId = postId;
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
