package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Comment;
import com.homework.web.service.CommentService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/comment")
@Api(tags = "共同前缀：/api/comment", description = "CommentController")
@Slf4j
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	UserService userService;

	@PostMapping
	@ApiOperation("新增Comment")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody Comment comment) {
		log.info("新增Comment");
		if (comment.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (comment.getNovel_id() == null) {
			throw new ControllerException("novel_id不可为null");
		} else if (comment.getContent() == null || comment.getContent().equals("")) {
			throw new ControllerException("content不可为null，也不可为空字符串");
		} else {
			comment.setUser_id(userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId());
			return new ResponseObject("200", "操作成功", commentService.insert(comment));
		}
	}

	@GetMapping
	@ApiOperation("查询Comment")
	public ResponseObject get(Integer novel_id, Integer user_id) {
		log.info("查询Comment");
		if (novel_id != null && user_id != null) {
			return new ResponseObject("200", "操作成功", commentService.selectByUser_idNovel_id(user_id, novel_id));
		} else if (novel_id != null && user_id == null) {
			return new ResponseObject("200", "操作成功", commentService.selectByNovel_id(novel_id));
		} else if (novel_id == null && user_id != null) {
			return new ResponseObject("200", "操作成功", commentService.selectByUser_id(user_id));
		} else {
			throw new ControllerException("novel_id和user_id不可同时为null");
		}
	}

	@GetMapping("/count")
	@ApiOperation("查询Comment")
	public ResponseObject getCount(Integer novel_id) {
		log.info("查询Comment");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", commentService.selectByNovel_id(novel_id).size());
		}
	}
}
