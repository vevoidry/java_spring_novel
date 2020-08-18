package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	@Query(value = "select * from comment where novel_id=:novel_id", nativeQuery = true)
	List<Comment> selectByNovel_id(@Param(value = "novel_id") Integer novel_id);

	@Query(value = "select * from comment where user_id=:user_id", nativeQuery = true)
	List<Comment> selectByUser_id(@Param(value = "user_id") Integer user_id);

	@Query(value = "select * from comment where user_id=:user_id and novel_id=:novel_id", nativeQuery = true)
	List<Comment> selectByUser_idNovel_id(@Param(value = "user_id") Integer user_id,
			@Param(value = "novel_id") Integer novel_id);
}
