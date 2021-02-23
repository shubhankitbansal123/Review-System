package com.example.repository;

import com.example.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {


    @Query(value = "select comment from comment where user_id=?1",nativeQuery = true)
    List<String> getCommentFromUser_id(Integer userId);
}
