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
import com.homework.web.pojo.Recommend;
import com.homework.web.pojo.User;
import com.homework.web.service.NovelService;
import com.homework.web.service.RecommendService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/recommend")
@Api(tags = "共同前缀：/api/recommend", description = "RecommendController")
@Slf4j
public class RecommendController {

	@Autowired
	RecommendService recommendService;
	@Autowired
	UserService userService;
	@Autowired
	NovelService novelService;

	@PostMapping
	@ApiOperation("新增Recommend")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody Recommend recommend) {
		log.info("新增Recommend");
		if (recommend.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (recommend.getNovel_id() == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			recommend.setUser_id(userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId());
			if (recommendService.selectByUser_idNovel_id(recommend.getUser_id(), recommend.getNovel_id()) != null) {
				throw new ControllerException("该用户已经推荐过该小说了，不可重复推荐");
			} else {
				return new ResponseObject("200", "操作成功", recommendService.insert(recommend));
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Recommend")
	public ResponseObject get(Integer novel_id, Integer user_id) {
		log.info("查询Recommend");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", recommendService.selectByUser_idNovel_id(user_id, novel_id));
		}
	}

	@DeleteMapping
	@ApiOperation("删除Recommend")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject delete(Integer novel_id, Integer user_id) {
		log.info("删除Recommend");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			Recommend recommend = recommendService.selectByUser_idNovel_id(user_id, novel_id);
			if (recommend == null) {
				throw new ControllerException("该用户还未推荐该小说，无法取消推荐");
			} else {
				User user = userService.selectByUsername(
						(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				if (user.getId() == recommend.getUser_id() || user.getRole().equals("ADMIN")) {
					recommendService.deleteById(recommend.getId());
					return new ResponseObject("200", "操作成功", null);
				} else {
					throw new ControllerException("该用户无权限取消推荐");
				}
			}
		}
	}

	@GetMapping("/count")
	@ApiOperation("查询Recommend")
	public ResponseObject getCount(Integer novel_id) {
		log.info("查询Recommend");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", recommendService.selectByNovel_id(novel_id).size());
		}
	}

	@GetMapping("/novel")
	@ApiOperation("查询Recommend的Novel")
	public ResponseObject getNovel(Integer user_id) {
		log.info("查询Recommend的Novel");
		if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", novelService.selectByUser_idOfRecommend(user_id));
		}
	}

}
