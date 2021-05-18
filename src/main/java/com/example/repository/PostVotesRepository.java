package com.example.repository;

import com.example.model.Post;
import com.example.model.PostVotes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVotesRepository extends CrudRepository<PostVotes, Integer> {

    int countPostVotesByPostIdAndValueEquals(int post_id, boolean value);

    List<PostVotes> findByPost(Post post);



}
