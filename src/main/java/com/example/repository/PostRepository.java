package com.example.repository;

import com.example.api.response.CountPostsByDate;
import com.example.model.ModerationStatus;
import com.example.model.Post;
import com.example.model.Tag;
import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query(value = "select p from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date")
    Page<Post> findActivePosts(Pageable pageable);

    @Query(value = "select p from Post p where p.id=:id")
    Post getPostById(@Param("id") int id);

    List<Post> findByUserId(int user_id);

    @Query(value = "SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user.id " +
            "LEFT JOIN PostComment pc ON pc.post.id = p.id " +
            "LEFT JOIN PostVotes pvl on pvl.post.id = p.id and pvl.value = true " +
            "WHERE p.isActive = true AND p.status = 'ACCEPTED' AND p.date <= CURRENT_DATE " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pvl) DESC")
    Page<Post> findLike(Pageable pageable);

    @Query(value = "select p from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date and p.title = :query")
    Page<Post> findPostsByQuery(@Param("query") String query, Pageable pageable);

    @Query(value = "select count(p) from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date")
    int countActive();

    @Query(value = "select new com.example.api.response.CountPostsByDate(function('date_format', p.date, '%Y-%m-%d'), count(p.date)) " +
            "from Post p where p.isActive = true and p.status = 'ACCEPTED'" +
            "and function('date_format', p.date, '%Y') = :year " +
            "and p.date <= current_date group by p.date order by p.date asc")
    List<CountPostsByDate> countPostsByDate(@Param("year") String year);

    @Query(value = "SELECT distinct substr(time,1, 4) as date FROM posts order by date;", nativeQuery = true)
    List<Integer> years();

    @Query(value = "select p from Post p where p.isActive = true and p.status = 'ACCEPTED' and function('date_format', p.date, '%Y-%m-%d') = :date")
    Page<Post> findPostsByDateEquals(@Param("date") String date, Pageable pageable);

    @Query(value = "SELECT * FROM degree_project.posts " +
                    "JOIN tag2post ON tag2post.post_id = posts.id " +
                    "JOIN tags ON tag2post.tag_id = tags.id " +
                    "WHERE tags.name = :tag AND posts.moderation_status = 'ACCEPTED' " +
                    "AND posts.is_active = 1 AND posts.time <= now()", nativeQuery = true)
    Page<Post> findPostsByTagsContains(@Param("tag") String tag, Pageable pageable);

    @Query(value = "select p from Post p where p.moderatorId = :id and p.status = :status and p.isActive = true")
    Page<Post> findByModeratorIdAndStatusEquals(@Param("id") int moderatorId, @Param("status") ModerationStatus status, Pageable pageable);

    @Query(value = "select p from Post p where p.user.id = :id and p.status = :status and p.isActive = :isActive order by p.date asc")
    Page<Post> findPostsByUserId(@Param("id") int userId,
                                 @Param("isActive") boolean isActive,
                                 @Param("status")ModerationStatus moderationStatus,
                                 Pageable pageable);

    List<Post> findPostsByStatus(ModerationStatus moderationStatus);

    @Query(value = "select p from Post p where p.isActive = true and p.status = 'ACCEPTED' and p.date <= current_date order by p.date asc")
    List<Post> findAllPosts();

}
