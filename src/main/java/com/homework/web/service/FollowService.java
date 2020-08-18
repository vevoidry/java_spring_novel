package com.homework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Follow;
import com.homework.web.repository.FollowRepository;

@Service
public class FollowService {
	@Autowired
	FollowRepository followRepository;

	public Follow insert(Follow follow) {
		return followRepository.save(follow);
	}

	public Follow selectByFollower_idFollowing_id(Integer follower_id, Integer following_id) {
		return followRepository.selectByFollower_idFollowing_id(follower_id, following_id);
	}

	public void deleteById(Integer id) {
		followRepository.deleteById(id);
	}
}
