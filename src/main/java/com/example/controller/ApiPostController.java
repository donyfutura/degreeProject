package com.example.controller;

import com.example.api.response.PostDTO;
import com.example.api.response.PostsResponse;
import com.example.model.Post;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private PostService postService;

    public ApiPostController( PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:write')")
    public PostsResponse allPosts(@RequestParam(defaultValue = "0") int offset,
                                  @RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(defaultValue = "recent") String mode){



        return postService.getPosts(offset, limit, mode);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('user:moderate')")
    public PostsResponse searchPosts(@RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "10") int limit,
                                     @RequestParam String query){
        if (query.isEmpty() || query.matches("\\s+")){
            return postService.getPosts(offset, limit, "recent");
        }

        return postService.searchPosts(offset, limit, query);
    }

    @GetMapping("/byDate")
    public PostsResponse postsByDate(@RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "10") int limit,
                                     @RequestParam String date){
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    public PostsResponse postsByTag(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "10") int limit,
                                    @RequestParam String tag){
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> postById(@PathVariable int id){
        return postService.getPostById(id);
    }


}

// http://localhost:8080/api/post?offset=0&limit=10&mode=recent