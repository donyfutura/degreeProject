package com.example.repository;

import com.example.model.PostVotes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends CrudRepository<PostVotes, Integer> {

    int countPostVotesByPostIdAndValueEquals(int post_id, boolean value);



}
