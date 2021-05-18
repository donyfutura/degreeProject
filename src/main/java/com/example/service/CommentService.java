package com.example.service;

import com.example.api.request.AddingCommentRequest;
import com.example.api.response.AddingCommentResponse;
import com.example.api.response.Error;
import com.example.model.PostComment;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<?> addComment(AddingCommentRequest addingCommentRequest, Authentication authentication) {
        Error error = new Error();
        Map<String, String> errors = new HashMap<>();
        if (addingCommentRequest.getText().length() < 1 || addingCommentRequest.getText() == null){
            errors.put("text", "Текст комментария не задан или слишком короткий");
            error.setErrors(errors);
            error.setResult(false);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        PostComment comment = new PostComment();
        comment.setUser(userRepository.findByEmail(authentication.getName()).get());
        comment.setDate(new Date());
        comment.setText(addingCommentRequest.getText());
        comment.setPost(postRepository.getPostById(Integer.parseInt(addingCommentRequest.getPostId())));

        if (!(addingCommentRequest.getParentId() == null )){
            comment.setParentId(Integer.parseInt(addingCommentRequest.getParentId()));
        }
        commentRepository.save(comment);
        return new ResponseEntity<>(new AddingCommentResponse(comment.getId()), HttpStatus.OK);


    }

}
