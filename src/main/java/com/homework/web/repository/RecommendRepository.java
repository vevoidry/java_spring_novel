package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer> {
	@Query(value = "select * from recommend where user_id = :user_id and novel_id=:novel_id", nativeQuery = true)
	Recommend selectByUser_idNovel_id(@Param(value = "user_id") Integer user_id,
			@Param(value = "novel_id") Integer novel_id);

	@Query(value = "select * from recommend where novel_id=:novel_id", nativeQuery = true)
	List<Recommend> selectByNovel_id(@Param(value = "novel_id") Integer novel_id);
}
