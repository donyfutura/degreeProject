package com.example.service;

import com.example.api.response.PostsResponse;
import com.example.api.response.innerObjects.User;
import com.example.model.Post;
import com.example.repository.PostRepository;
import com.example.repository.PostVotesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Numbers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostVotesRepository postVotesRepository;

    public PostService(PostRepository postRepository, PostVotesRepository postVotesRepository) {
        this.postRepository = postRepository;
        this.postVotesRepository = postVotesRepository;
    }

    public PostsResponse getPosts(String offset, String limit, String mode){

        List<Post> postsDB = null;

        switch (mode){
            case "recent":
                Pageable sortedByNew = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("date").descending());
                postsDB = postRepository.findActivePosts(sortedByNew);
                break;
            case "popular":
                Pageable sortedByComments = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("comments.size").descending());
                postsDB = postRepository.findActivePosts(sortedByComments);
                break;
            case "best":
                Pageable sortedByLikes = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit));
                postsDB = postRepository.findLike(sortedByLikes);
                break;
            case "early":
                Pageable sortedByOld = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("date"));
                postsDB = postRepository.findActivePosts(sortedByOld);
                break;
        }
        List<com.example.api.response.innerObjects.Post> postsForResponse = new ArrayList<>();

        for (Post post : postsDB){
            com.example.api.response.innerObjects.Post p = new com.example.api.response.innerObjects.Post();
            p.setId(post.getId());
            Long time = post.getDate().getTime();
            p.setTimestamp(Long.parseLong(time.toString().substring(0, 10)));
            p.setUser(new User(post.getUser().getId(), post.getUser().getName()));
            p.setTitle(post.getTitle());
            String text = post.getText().length() > 150 ? (post.getText().substring(0, 150) + "...") : post.getText();
            p.setAnnounce(text);
            p.setLikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(post.getId(), true));
            p.setDislikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(post.getId(), false));
            p.setCommentCount(post.getComments().size());
            p.setViewCount(post.getViewCount());
            postsForResponse.add(p);
        }
        return new PostsResponse(postsDB.size(), postsForResponse);

    }
}
