package com.example.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeRequest {

    @JsonProperty("post_id")
    private int postId;

    public LikeRequest(int postId) {
        this.postId = postId;
    }

    public LikeRequest() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
