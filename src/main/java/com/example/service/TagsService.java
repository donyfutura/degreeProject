package com.example.service;

import com.example.api.response.TagsResponse;
import com.example.model.Tag;
import com.example.repository.PostRepository;
import com.example.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class TagsService {

    private final PostRepository postRepository;

    private final TagRepository tagRepository;

    public TagsService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public TagsResponse getTags(String query) {

        List<com.example.api.response.innerObjects.Tag> tagsForResponse = new ArrayList<>();
        long count = postRepository.count();
        double k;
        List<Tag> tags = tagRepository.findAll();
        for(Tag tag : tags){
            tagsForResponse.add(new com.example.api.response.innerObjects.Tag(tag.getName(), ((double) tag.getPosts().size() / count)));
        }

        tagsForResponse.sort(Comparator.comparingDouble(com.example.api.response.innerObjects.Tag::getWeight).reversed());
        k = 1 / tagsForResponse.get(0).getWeight();
        System.out.println();


        if (query.equals("empty")){
            for (com.example.api.response.innerObjects.Tag tag : tagsForResponse) {
                tag.setWeight(k * tag.getWeight());
            }
            return new TagsResponse(tagsForResponse);

        } else {
            Tag tag = tagRepository.findTagByNameStartsWith(query);
            if (tag == null){
                return null;
            }
            List<com.example.api.response.innerObjects.Tag> tags1 = new ArrayList<>();

            tags1.add(new com.example.api.response.innerObjects.Tag(tag.getName(), (double) tag.getPosts().size()/count * k));
            return new TagsResponse(tags1);

        }

    }
}
