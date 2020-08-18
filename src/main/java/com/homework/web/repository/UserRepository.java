package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value = "select * from user where username = :username", nativeQuery = true)
	User selectByUsername(@Param(value = "username") String username);

	@Query(value = "SELECT user.* FROM user WHERE user.id = ANY(SELECT follow.follower_id FROM follow WHERE follow.following_id = :following_id)", nativeQuery = true)
	List<User> selectByFollowing_id(@Param(value = "following_id") Integer following_id);
	
	@Query(value = "SELECT user.* FROM user WHERE user.id = ANY(SELECT follow.following_id FROM follow WHERE follow.follower_id = :follower_id)", nativeQuery = true)
	List<User> selectByFollower_id(@Param(value = "follower_id") Integer follower_id);
}
