package com.example.service;

import com.example.api.response.PostsResponse;
import com.example.api.response.innerObjects.Post;
import com.example.api.response.innerObjects.User;
import com.example.repository.PostRepository;
import com.example.repository.PostVotesRepository;
import org.springframework.stereotype.Service;

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
        List<com.example.model.Post> posts = postRepository.findAll();

        int lim = Math.min(Integer.parseInt(limit), posts.size());

        switch (mode) {
            case "popular":
                posts.sort(Comparator.comparingInt(o -> -1 *o.getComments().size()));
                break;
            case "best":
                // ПРоблема
                posts.sort(Comparator.comparingInt(o -> -1 * o.getVotes().size()));
                break;
            case "early":
                posts.sort(Comparator.comparing(com.example.model.Post::getDate));
                break;
            case "recent":
                posts.sort(Comparator.comparing(com.example.model.Post::getDate).reversed());
        }

        for (int i = Integer.parseInt(offset); i < lim; i++){
            Post post = new Post();
            post.setId(posts.get(i).getId());

            Long time = posts.get(i).getDate().getTime();
            post.setTimestamp(Long.parseLong(time.toString().substring(0, 10)));

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
            list.add(post);
        }

        PostsResponse postsResponse = new PostsResponse(posts.size(), list);

        return postsResponse;
    }
}
