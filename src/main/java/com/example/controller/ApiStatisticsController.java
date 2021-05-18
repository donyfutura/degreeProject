package com.example.controller;

import com.example.api.response.StatisticsResponse;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class ApiStatisticsController {

    private final PostService postService;

    @Autowired
    public ApiStatisticsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('user:write')")
    public ResponseEntity<StatisticsResponse> myStats(Authentication authentication){
        return postService.myStatistics(authentication);
    }

    @GetMapping("/all")
    public ResponseEntity<StatisticsResponse> allStats(Authentication authentication){
        return postService.allStatistics(authentication);
    }

}
