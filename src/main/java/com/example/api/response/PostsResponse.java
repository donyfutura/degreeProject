package com.example.api.response;

import com.example.api.response.innerObjects.Post;
import com.example.api.response.innerObjects.PostDTO;
import org.springframework.stereotype.Component;

import java.util.List;


public class PostsResponse {

    private long count;

    private List<PostDTO> posts;

    public PostsResponse(long count, List<PostDTO> postsList) {
        this.count = count;
        this.posts = postsList;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> postsList) {
        this.posts = postsList;
    }
}

