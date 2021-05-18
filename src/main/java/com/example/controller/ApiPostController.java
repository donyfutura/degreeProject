package com.example.controller;

import com.example.api.request.AddingPostRequest;
import com.example.api.request.LikeRequest;
import com.example.api.response.AddingPostResponse;
import com.example.api.response.PostDTO;
import com.example.api.response.PostsResponse;
import com.example.model.ModerationStatus;
import com.example.model.Post;
import com.example.model.User;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private PostService postService;

    public ApiPostController( PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PostsResponse allPosts(@RequestParam(defaultValue = "0") int offset,
                                  @RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(defaultValue = "recent") String mode){



        return postService.getPosts(offset, limit, mode);
    }

    @GetMapping("/search")
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

    @GetMapping("/moderation")
    @PreAuthorize("hasAnyAuthority('user:moderate')")
    public ResponseEntity<PostsResponse> moderationPosts(@RequestParam(defaultValue = "0") int offset,
                                                        @RequestParam(defaultValue = "10") int limit,
                                                        @RequestParam String status,
                                                         Authentication authentication){
        return postService.getModerationPosts(offset, limit, status, authentication.getName());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<PostsResponse> myPosts(@RequestParam(defaultValue = "0") int offset,
                                                 @RequestParam(defaultValue = "10") int limit,
                                                 @RequestParam String status,
                                                 Authentication authentication){
        return postService.getMyPosts(offset, limit, status, authentication.getName());
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<AddingPostResponse> addPost(@RequestBody AddingPostRequest request,
                                                      Authentication authentication){
        return postService.addPost(request.getTimestamp(), request.getActive(), request.getTitle(), request.getTags(), request.getText(), authentication);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<AddingPostResponse> editPost(@RequestBody AddingPostRequest request,
                                                       @PathVariable int id,
                                                       Authentication authentication){
        return postService.editPost(request, authentication, id);
    }

    @PostMapping("/like")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<Map> likePost(@RequestBody LikeRequest likeRequest,
                                        Authentication authentication){
        return postService.like(likeRequest, authentication);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<Map> dislikePost(@RequestBody LikeRequest likeRequest,
                                        Authentication authentication){
        return postService.dislike(likeRequest, authentication);
    }





}

// http://localhost:8080/api/post?offset=0&limit=10&mode=recent