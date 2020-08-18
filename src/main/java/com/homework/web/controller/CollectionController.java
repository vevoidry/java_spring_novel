package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Collection;
import com.homework.web.pojo.User;
import com.homework.web.service.CollectionService;
import com.homework.web.service.NovelService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/collection")
@Api(tags = "共同前缀：/api/collection", description = "CollectionController")
@Slf4j
public class CollectionController {
	@Autowired
	CollectionService collectionService;
	@Autowired
	UserService userService;
	@Autowired
	NovelService novelService;

	@PostMapping
	@ApiOperation("新增Collection")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(Collection collection) {
		log.info("新增Collection");
		if (collection.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (collection.getNovel_id() == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			collection.setUser_id(userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getId());
			if (collectionService.selectByUser_idNovel_id(collection.getUser_id(), collection.getNovel_id()) != null) {
				throw new ControllerException("该用户已经收藏过该小说了，不可重复收藏");
			} else {
				return new ResponseObject("200", "操作成功", collectionService.insert(collection));
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Collection")
	public ResponseObject get(Integer novel_id, Integer user_id) {
		log.info("查询Collection");
		if (novel_id != null && user_id != null) {
			return new ResponseObject("200", "操作成功", collectionService.selectByUser_idNovel_id(user_id, novel_id));
		} else if (novel_id != null && user_id == null) {
			return new ResponseObject("200", "操作成功", collectionService.selectByNovel_id(novel_id));
		} else if (novel_id == null && user_id != null) {
			return new ResponseObject("200", "操作成功", collectionService.selectByUser_id(user_id));
		} else {
			throw new ControllerException("novel_id与user_id不可同时为null");
		}
	}

	@GetMapping("/novel")
	@ApiOperation("查询Collection的Novel")
	public ResponseObject getNovel(Integer user_id) {
		log.info("查询Collection的Novel");
		if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", novelService.selectByUser_idOfCollection(user_id));
		}
	}

	@DeleteMapping
	@ApiOperation("删除Collection")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject delete(Integer novel_id, Integer user_id) {
		log.info("删除Collection");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else if (user_id == null) {
			throw new ControllerException("user_id不可为null");
		} else {
			Collection collection = collectionService.selectByUser_idNovel_id(user_id, novel_id);
			if (collection == null) {
				throw new ControllerException("该用户还未收藏该小说，无法取消收藏");
			} else {
				User user = userService.selectByUsername(
						(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				if (user.getId() == user_id || user.getRole().equals("ADMIN")) {
					collectionService.deleteById(collection.getId());
					return new ResponseObject("200", "操作成功", null);
				} else {
					throw new ControllerException("该用户无权限取消收藏");
				}
			}
		}
	}

	@GetMapping("/count")
	@ApiOperation("查询Collection")
	public ResponseObject getCount(Integer novel_id) {
		log.info("查询Collection");
		if (novel_id == null) {
			throw new ControllerException("novel_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", collectionService.selectByNovel_id(novel_id).size());
		}
	}
}
