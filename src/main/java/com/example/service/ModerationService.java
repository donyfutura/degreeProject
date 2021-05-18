package com.example.service;

import com.example.api.request.ModerationRequest;
import com.example.api.response.ModerationResponse;
import com.example.model.ModerationStatus;
import com.example.model.Post;
import com.example.model.User;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ModerationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ModerationService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ModerationResponse> setup(ModerationRequest moderationRequest, Authentication authentication){
        Post post = postRepository.getPostById(moderationRequest.getPostId());
        User user = userRepository.findByEmail(authentication.getName()).get();
        post.setModeratorId(user.getId());
        post.setDate(new Date());

        switch (moderationRequest.getDecision()){
            case "accept": post.setStatus(ModerationStatus.ACCEPTED); break;
            case "decline": post.setStatus(ModerationStatus.DECLINED); break;
        }
        postRepository.save(post);
        return new ResponseEntity<>(new ModerationResponse(true), HttpStatus.OK);
    }

}
