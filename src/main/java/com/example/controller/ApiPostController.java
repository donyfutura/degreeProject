package com.example.controller;

import com.example.api.response.PostsResponse;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private PostService postService;

    public ApiPostController( PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostsResponse allPosts(@RequestParam(defaultValue = "0") String offset,
                                  @RequestParam(defaultValue = "10") String limit,
                                  @RequestParam(defaultValue = "recent") String mode){



        return postService.getPosts(offset, limit, mode);
    }

}

// http://localhost:8080/api/post?offset=0&limit=10&mode=recent