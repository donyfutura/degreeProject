package com.example.api.response;

import com.example.api.response.innerObjects.Post;

import java.util.List;

public class PostsResponse {

    private int count;

    private List<Post> postsList;

    public PostsResponse(int count, List<Post> postsList) {
        this.count = count;
        this.postsList = postsList;
    }
}

