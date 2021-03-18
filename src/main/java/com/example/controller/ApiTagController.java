package com.example.controller;


import com.example.api.response.TagsResponse;
import com.example.service.TagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tag")
public class ApiTagController {

    private TagsService tagsService;

    public ApiTagController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping
    public TagsResponse getTags(@RequestParam(defaultValue = "empty") String query){
        return tagsService.getTags(query);
    }
}
