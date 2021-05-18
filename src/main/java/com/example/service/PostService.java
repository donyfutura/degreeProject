package com.example.service;

import com.example.api.request.AddingPostRequest;
import com.example.api.request.LikeRequest;
import com.example.api.response.*;
import com.example.api.response.innerObjects.PostDTO;
import com.example.api.response.innerObjects.User;
import com.example.comparator.CommentsComparator;
import com.example.model.*;
import com.example.repository.PostRepository;
import com.example.repository.PostVotesRepository;
import com.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
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


    public ResponseEntity<AddingPostResponse> addPost(Long timestamp, int active, String title, String[] tags, String text, Authentication authentication) {
        boolean flag = true;
        AddingPostResponse addingPostResponse = new AddingPostResponse();
        HashMap<String, String> map = new HashMap<>();
        if (title.length() < 3){
            map.put("title", "Заголовок публикации слишком короткий");
            flag = false;
        }
        if (text.length() < 50){
            map.put("text", "Текст публикации слишком короткий");
            flag = false;
        }

        if (flag){
            Post newPost = new Post();
            Set<Tag> tagsForPost = new HashSet<>();
            for (String tag : tags){
                Tag newTag = new Tag();
                newTag.setName(tag);
                tagsForPost.add(newTag);
            }
            if (Instant.now().isAfter(Instant.ofEpochMilli(timestamp))) {
                newPost.setDate(new Date());
            } else {
               newPost.setDate(Date.from(Instant.ofEpochMilli(timestamp)));
            }
            com.example.model.User user = userRepository.findByEmail(authentication.getName()).get();
            newPost.setUser(user);
            newPost.setTitle(title);
            newPost.setTags(tagsForPost);
            newPost.setText(text);
            newPost.setActive(active == 1);
            newPost.setStatus(ModerationStatus.NEW);
            newPost.setTags(tagsForPost);
            postRepository.save(newPost);
            addingPostResponse.setResult(true);
        } else {
            addingPostResponse.setResult(false);
            addingPostResponse.setErrors(map);
        }

        return new ResponseEntity<>(addingPostResponse, HttpStatus.OK);

    }

    public ResponseEntity<AddingPostResponse> editPost(AddingPostRequest request, Authentication authentication, int id) {
        boolean flag = true;
        AddingPostResponse addingPostResponse = new AddingPostResponse();
        HashMap<String, String> map = new HashMap<>();
        com.example.model.User user = userRepository.findByEmail(authentication.getName()).get();
        if (request.getTitle().length() < 3){
            map.put("title", "Заголовок публикации слишком короткий");
            flag = false;
        }
        if (request.getText().length() < 50){
            map.put("text", "Текст публикации слишком короткий");
            flag = false;
        }

        if (flag){
            Post newPost = postRepository.getPostById(id);
            Set<Tag> tagsForPost = new HashSet<>();
            for (String tag : request.getTags()){
                Tag newTag = new Tag();
                newTag.setName(tag);
                tagsForPost.add(newTag);
            }
            if (Instant.now().isAfter(Instant.ofEpochMilli(request.getTimestamp()))) {
                newPost.setDate(new Date());
            } else {
                newPost.setDate(Date.from(Instant.ofEpochMilli(request.getTimestamp())));
            }
            newPost.setTitle(request.getTitle());
            newPost.setTags(tagsForPost);
            newPost.setText(request.getText());
            newPost.setActive(request.getActive() == 1);
            newPost.setStatus(ModerationStatus.NEW);
            newPost.setTags(tagsForPost);
            postRepository.save(newPost);
            addingPostResponse.setResult(true);
        } else {
            addingPostResponse.setResult(false);
            addingPostResponse.setErrors(map);
        }

        return new ResponseEntity<>(addingPostResponse, HttpStatus.OK);
    }

    public ResponseEntity<Map> like(LikeRequest likeRequest, Authentication authentication) {
            Post post = postRepository.getPostById(likeRequest.getPostId());
            PostVotes like = new PostVotes();
            List<PostVotes> votes = postVotesRepository.findByPost(post).stream()
                    .filter(v -> v.getUser().getEmail().equals(authentication.getName()))
                    .collect(Collectors.toList());
            if (votes.isEmpty()){
                like.setUser(userRepository.findByEmail(authentication.getName()).get());
                like.setDate(new Date());
                like.setPost(post);
                like.setValue(true);
                postVotesRepository.save(like);
            } else {
                PostVotes vote = votes.get(0);
                vote.setValue(true);
                postVotesRepository.save(vote);
            }
        return new ResponseEntity<>(Map.of("result", true), HttpStatus.OK);

    }

    public ResponseEntity<Map> dislike(LikeRequest likeRequest, Authentication authentication){
        Post post = postRepository.getPostById(likeRequest.getPostId());
        PostVotes dislike = new PostVotes();
        List<PostVotes> votes = postVotesRepository.findByPost(post).stream()
                .filter(v -> v.getUser().getEmail().equals(authentication.getName()))
                .collect(Collectors.toList());
        if (votes.isEmpty()){
            dislike.setUser(userRepository.findByEmail(authentication.getName()).get());
            dislike.setDate(new Date());
            dislike.setPost(post);
            dislike.setValue(false);
            postVotesRepository.save(dislike);
        } else {
            PostVotes vote = votes.get(0);
            vote.setValue(false);
            postVotesRepository.save(vote);
        }
        return new ResponseEntity<>(Map.of("result", true), HttpStatus.OK);
    }

    public ResponseEntity<StatisticsResponse> myStatistics(Authentication authentication){
        List<Post> posts = postRepository.findPostsByUserId(userRepository.findByEmail(authentication.getName()).get().getId(),
                                                    true,
                                                            ModerationStatus.ACCEPTED,
                                                            Pageable.unpaged()).toList();
        return countStats(posts);
    }

    public ResponseEntity<StatisticsResponse> allStatistics(Authentication authentication){
        List<Post> posts = postRepository.findAllPosts();
        return countStats(posts);
    }

    private ResponseEntity<StatisticsResponse> countStats(List<Post> posts){
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        int likes = 0;
        int dislikes = 0;
        int views = 0;
        for (Post post: posts){
            views += post.getViewCount();
            for (PostVotes votes : post.getVotes()){
                if (votes.isValue()){
                    likes++;
                } else {
                    dislikes++;
                }
            }
        }
        statisticsResponse.setPostsCount(posts.size());
        statisticsResponse.setViewsCount(views);
        statisticsResponse.setFirstPublication(Long.parseLong(String.valueOf(posts.get(0).getDate().getTime()).substring(0, 10)));
        statisticsResponse.setLikesCount(likes);
        statisticsResponse.setDislikesCount(dislikes);

        return new ResponseEntity<>(statisticsResponse, HttpStatus.OK);
    }


}
