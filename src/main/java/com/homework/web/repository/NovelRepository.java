package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Novel;

public interface NovelRepository extends JpaRepository<Novel, Integer> {
	@Query(value = "select * from novel where category_id = :category_id", nativeQuery = true)
	List<Novel> selectByCategory_id(@Param(value = "category_id") Integer category_id);

	@Query(value = "SELECT novel.* FROM novel LEFT OUTER JOIN recommend ON novel.id=recommend.novel_id WHERE novel.insert_date>:day GROUP BY novel.id ORDER BY novel.multiplier*(SELECT COUNT(id) FROM recommend WHERE novel_id=novel.id)+novel.addend DESC LIMIT :limit_count", nativeQuery = true)
	List<Novel> selectByRank(@Param(value = "day") String day, @Param(value = "limit_count") Integer limit_count);

	@Query(value = "select * from novel where user_id = :user_id", nativeQuery = true)
	List<Novel> selectByUser_id(@Param(value = "user_id") Integer user_id);

	@Query(value = "select * from novel where name like %:name%", nativeQuery = true)
	List<Novel> selectByLikeName(@Param(value = "name") String name);

	@Query(value = "SELECT novel.* FROM novel WHERE novel.id = ANY(SELECT collection.novel_id FROM collection WHERE collection.user_id = :user_id)", nativeQuery = true)
	List<Novel> selectByUser_idOfCollection(@Param(value = "user_id") Integer user_id);
	
	@Query(value = "SELECT novel.* FROM novel WHERE novel.id = ANY(SELECT recommend.novel_id FROM recommend WHERE recommend.user_id = :user_id)", nativeQuery = true)
	List<Novel> selectByUser_idOfRecommend(@Param(value = "user_id") Integer user_id);
}
