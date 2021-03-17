package com.example.repository;

import com.example.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findByUserId(int user_id);

    /*@Query("select * from posts where is_active = true")*/
    List<Post> findAllByActive(boolean active);



}
