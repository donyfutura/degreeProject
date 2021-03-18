package com.example.api.response;

import com.example.api.response.innerObjects.Post;
import org.springframework.stereotype.Component;

import java.util.List;


public class PostsResponse {

    private int count;

    private List<Post> posts;

    public PostsResponse(int count, List<Post> postsList) {
        this.count = count;
        this.posts = postsList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> postsList) {
        this.posts = postsList;
    }
}

