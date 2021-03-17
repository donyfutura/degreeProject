package com.example.service;

import com.example.api.response.PostsResponse;
import com.example.api.response.innerObjects.Post;
import com.example.api.response.innerObjects.User;
import com.example.repository.PostRepository;
import com.example.repository.PostVotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Post> list = new ArrayList<>();
        List<com.example.model.Post> posts = postRepository.findAllByActive(true);
/*
        if (mode.equals("popular")){

        } else if (mode.equals("best")){
            int likes = postVotesRepository.countPostVotesByPostIdAndValueEquals(posts.get(i).getId(), true);
            posts.sort();
        } else if (mode.equals("early")){
            posts.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        } else {
            posts.sort(((o1, o2) -> ));
        }*/

        for (int i = Integer.parseInt(offset); i < Integer.parseInt(limit); i++){
            Post post = new Post();
            post.setId(posts.get(i).getId());
            post.setTimestamp(posts.get(i).getDate().getTime());

            User user = new User();
            user.setId(posts.get(i).getUser().getId());
            user.setName(posts.get(i).getUser().getName());
            post.setUser(user);

            post.setTitle(posts.get(i).getTitle());
            String str = posts.get(i).getText().length() > 150 ? posts.get(i).getText()
                    .substring(0, 150) + "..." : posts.get(i).getText();
            post.setAnnounce(str);
            post.setLikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(posts.get(i).getId(), true));
            post.setDislikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(posts.get(i).getId(), false));
            post.setViewCount(posts.get(i).getViewCount());
            post.setCommentCount(posts.get(i).getComments().size());
        }
        PostsResponse postsResponse = new PostsResponse(posts.size(), list);

        return postsResponse;
    }
}
