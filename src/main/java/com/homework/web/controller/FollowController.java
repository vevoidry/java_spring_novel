package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Follow;
import com.homework.web.service.FollowService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/follow")
@Api(tags = "共同前缀：/api/follow", description = "FollowController")
@Slf4j
public class FollowController {
	@Autowired
	UserService userService;
	@Autowired
	FollowService followService;

	@PostMapping
	@ApiOperation("新增Follow")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody Follow follow) {
		log.info("新增Follow");
		System.out.println(follow);
		if (follow.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (follow.getFollower_id() != null) {
			throw new ControllerException("follower_id必须为null");
		} else if (follow.getFollowing_id() == null) {
			throw new ControllerException("following_id不可为null");
		} else {
			follow.setFollower_id(userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId());
			if (follow.getFollower_id() == follow.getFollowing_id()) {
				throw new ControllerException("不可自己关注自己");
			} else if (followService.selectByFollower_idFollowing_id(follow.getFollower_id(),
					follow.getFollowing_id()) != null) {
				throw new ControllerException("该用户已经关注过了，不可重复关注");
			} else {
				return new ResponseObject("200", "操作成功", followService.insert(follow));
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Follow")
	public ResponseObject get(Integer follower_id, Integer following_id) {
		log.info("查询Follow");
		if (follower_id != null && following_id != null) {
			return new ResponseObject("200", "操作成功",
					followService.selectByFollower_idFollowing_id(follower_id, following_id));
		} else if (follower_id != null && following_id == null) {
			return new ResponseObject("200", "操作成功", userService.selectByFollower_id(follower_id));
		} else if (follower_id == null && following_id != null) {
			return new ResponseObject("200", "操作成功", userService.selectByFollowing_id(following_id));
		} else {
			throw new ControllerException("follower_id与following_id不可同时为null");
		}
	}

	@DeleteMapping
	@ApiOperation("删除Follow")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject delete(Integer following_id) {
		log.info("删除Follow");
		if (following_id == null) {
			throw new ControllerException("following_id不可为null");
		} else {
			Integer follower_id = userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId();
			Follow follow = followService.selectByFollower_idFollowing_id(follower_id, following_id);
			if (follow == null) {
				throw new ControllerException("该用户未关注，无法取消关注");
			} else {
				followService.deleteById(follow.getId());
				return new ResponseObject("200", "操作成功", null);
			}
		}
	}
}
