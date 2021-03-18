package com.example.api.response;

import com.example.api.response.innerObjects.Tag;

import java.util.List;

public class TagsResponse {

    private List<Tag> tags;

    public TagsResponse(List<Tag> tags) {
        this.tags = tags;
    }

    public TagsResponse() {
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
