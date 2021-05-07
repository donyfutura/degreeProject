package com.example.service;

import com.example.api.response.*;
import com.example.api.response.innerObjects.PostDTO;
import com.example.api.response.innerObjects.User;
import com.example.comparator.CommentsComparator;
import com.example.model.ModerationStatus;
import com.example.model.Post;
import com.example.model.PostComment;
import com.example.model.Tag;
import com.example.repository.PostRepository;
import com.example.repository.PostVotesRepository;
import com.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final PostVotesRepository postVotesRepository;

    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, PostVotesRepository postVotesRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postVotesRepository = postVotesRepository;
        this.userRepository = userRepository;
    }
    public ResponseEntity<PostsResponse> getMyPosts(int offset, int limit, String status, String userName) {
        Optional<com.example.model.User> user = userRepository.findByEmail(userName);
        int userId = user.get().getId();
        Page<Post> postsDB = null;
        Pageable pageable = PageRequest.of(offset, limit);
        switch (status){
            case "inactive": postsDB = postRepository.findPostsByUserId(userId, false, null, pageable); break;
            case "pending": postsDB = postRepository.findPostsByUserId(userId, true, ModerationStatus.NEW, pageable); break;
            case "declined": postsDB = postRepository.findPostsByUserId(userId, true, ModerationStatus.DECLINED, pageable); break;
            case "published": postsDB = postRepository.findPostsByUserId(userId, true, ModerationStatus.ACCEPTED, pageable); break;
        }
        PostsResponse response =  fillPostResponse(postsDB);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<PostsResponse> getModerationPosts(int offset, int limit, String status, String userName){
        Optional<com.example.model.User> user = userRepository.findByEmail(userName);
        int userId = user.get().getId();
        ModerationStatus moderationStatus;
        switch (status){
            case "new": moderationStatus = ModerationStatus.NEW; break;
            case "accepted": moderationStatus = ModerationStatus.ACCEPTED; break;
            case "declined": moderationStatus = ModerationStatus.DECLINED; break;
            default: moderationStatus = null;
        }
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Post> postsDB = postRepository.findByModeratorIdAndStatusEquals(userId, moderationStatus, pageable);
        System.out.println(postsDB);

        PostsResponse response = fillPostResponse(postsDB);
        System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<com.example.api.response.PostDTO> getPostById(int id){
        Post post = postRepository.getPostById(id);
        if (post == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        com.example.api.response.PostDTO postDTO = new com.example.api.response.PostDTO();
        postDTO.setId(post.getId());
        postDTO.setActive(true);
        long time = post.getDate().getTime();
        postDTO.setTimestamp(Long.parseLong(Long.toString(time).substring(0, 10)));
        UserDTO user = new UserDTO();
        user.setId(post.getUser().getId());
        user.setName(post.getUser().getName());
        user.setPhoto(post.getUser().getPhoto());
        postDTO.setUser(user);
        postDTO.setTitle(post.getTitle());
        postDTO.setText(post.getText());
        postDTO.setViewCount(post.getViewCount());
        postDTO.setLikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(post.getId(), true));
        postDTO.setDislikeCount(postVotesRepository.countPostVotesByPostIdAndValueEquals(post.getId(), false));
        List<CommentDto> comments = new ArrayList<>();
        for (PostComment postComment: post.getComments()){
            CommentDto commentDto = new CommentDto(postComment);
            comments.add(commentDto);
        }
        comments.sort(new CommentsComparator());
        postDTO.setComments(comments);
        postDTO.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        return new ResponseEntity<com.example.api.response.PostDTO>(postDTO, HttpStatus.OK);
    }

    public PostsResponse getPostsByTag(int offset, int limit, String tag){
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Post> posts = postRepository.findPostsByTagsContains(tag, pageable);
        return fillPostResponse(posts);
    }

    public PostsResponse getPostsByDate(int offset, int limit, String date){
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("date"));
        Page<Post> posts = postRepository.findPostsByDateEquals(date, pageable);
        return fillPostResponse(posts);
    }

    public CalendarResponse getCountsPostsByDate(int year){
        CalendarResponse calendarResponse = new CalendarResponse();
        HashMap<String, Long> map = new HashMap<>();
        List<Integer> years = postRepository.years();
        calendarResponse.setYears(years);
        List<CountPostsByDate> countPostsByDates = postRepository.countPostsByDate(Integer.toString(year));
        for (CountPostsByDate c: countPostsByDates) {
            map.put(c.getDate(), c.getCount());
        }
        calendarResponse.setPosts(map);
        return calendarResponse;

    }

    public PostsResponse searchPosts(int offset, int limit, String query){
        Page<Post> postsDB;

        Pageable pageable = PageRequest.of(offset, limit);
        postsDB = postRepository.findPostsByQuery(query, pageable);

        return fillPostResponse(postsDB);
    }

    public PostsResponse getPosts(int offset, int limit, String mode){
        Page<Post> postsDB = null;

        switch (mode){
            case "recent":
                Pageable sortedByNew = PageRequest.of(offset, limit, Sort.by("date").descending());
                postsDB = postRepository.findActivePosts(sortedByNew);
                break;
            case "popular":
                Pageable sortedByComments = PageRequest.of(offset, limit, Sort.by("comments.size").descending());
                postsDB = postRepository.findActivePosts(sortedByComments);
                break;
            case "best":
                Pageable sortedByLikes = PageRequest.of(offset, limit);
                postsDB = postRepository.findLike(sortedByLikes);
                break;
            case "early":
                Pageable sortedByOld = PageRequest.of(offset, limit, Sort.by("date"));
                postsDB = postRepository.findActivePosts(sortedByOld);
                break;
        }

        return fillPostResponse(postsDB);
    }

    private PostsResponse fillPostResponse(Page<Post> posts){
        List<PostDTO> postsForResponse = new ArrayList<>();

        for (Post post : posts){
            PostDTO p  = new PostDTO();
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
        return new PostsResponse(posts.getTotalElements(), postsForResponse);
    }


}
