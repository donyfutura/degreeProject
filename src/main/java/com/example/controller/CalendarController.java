package com.example.controller;

import com.example.api.response.CalendarResponse;
import com.example.api.response.CountPostsByDate;
import com.example.api.response.PostsResponse;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private final PostService postService;

    public CalendarController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public CalendarResponse getCountPostsByDate(@RequestParam(defaultValue = "2021") Integer year){

        return postService.getCountsPostsByDate(year);
    }


}
