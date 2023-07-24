package com.example.webblog.repositories;

import com.example.webblog.entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.userId = :user_id")
    List<Post> findPostsByUserId(@Param("user_id") Long userId);
}
