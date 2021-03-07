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

    @Query(value = "select case when exists(select * from comment where comment_id=?1 and user_id=?2) then true else false end",nativeQuery = true)
    boolean checkCommentIdAndUserId(Integer commentId,Integer userId);

    @Query(value = "update comment set comment=?2 where comment_id=?1 returning true",nativeQuery = true)
    boolean updateComment(Integer commentId, String comment);

    @Query(value = "delete from comment where type_id=?1 returning true",nativeQuery = true)
    boolean deleteDataOfHotel(Integer id);

    @Query(value = "select comment from comment where type_id=?1",nativeQuery = true)
    List<String> getCommentFromType_id(Integer typeId);

    @Query(value = "select * from comment where type=?1 order by type_id",nativeQuery = true)
    List<Comment> findCommentByType(String type);

    @Query(value = "select case when exists(select * from comment where type_id=?1) then true else false end",nativeQuery = true)
    boolean getCommentCount(Integer id);
}
