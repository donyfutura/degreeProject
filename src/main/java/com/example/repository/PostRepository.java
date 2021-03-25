package com.example.repository;

import com.example.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query(value = "select p from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date")
    Page<Post> findActivePosts(Pageable pageable);

    List<Post> findByUserId(int user_id);

    @Query(value = "SELECT *, (SELECT COUNT(*) FROM post_votes WHERE posts.id = post_votes.post_id AND post_votes.value > 0) as likes FROM posts WHERE " +
            "            posts.is_active = true AND posts.moderation_status = 'ACCEPTED' AND posts.time <= now() ORDER BY likes DESC", nativeQuery = true)
    Page<Post> findLike(Pageable pageable);

    @Query(value = "select count(p) from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date")
    int countActive();
}
