package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.web.service.NovelService;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Api(tags = "共同前缀：/api", description = "AppController")
@Slf4j
public class AppController {
	@Autowired
	UserService userService;
	@Autowired
	NovelService novelService;

	@GetMapping("/me")
	@ApiOperation("查询Me")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject getMe() {
		log.info("查询Me");
		return new ResponseObject("200", "操作成功", userService
				.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
	}

}
