package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
	@Query(value = "select * from follow where follower_id=:follower_id and following_id=:following_id", nativeQuery = true)
	Follow selectByFollower_idFollowing_id(@Param(value = "follower_id") Integer follower_id,
			@Param(value = "following_id") Integer following_id);
}
