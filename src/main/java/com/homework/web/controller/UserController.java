package com.homework.web.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.User;
import com.homework.web.service.UserService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Api(tags = "共同前缀：/api/user", description = "UserController")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	@ApiOperation("新增User")
	public ResponseObject post(String username, String password) {
		log.info("新增User");
		if (username == null || username.equals("")) {
			throw new ControllerException("username不可为null，也不可为空字符串");
		} else if (password == null || password.equals("")) {
			throw new ControllerException("password不可为null，也不可为空字符串");
		} else if (userService.selectByUsername(username) != null) {
			throw new ControllerException("该username已被使用");
		} else {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setRole("VIP1");
			user.setNickname(new Date().getTime() + "");
			user.setImage("default_user_image.png");
			user.setEmail("该用户没有填写邮箱");
			user.setPhone("该用户没有填写手机号码");
			user.setProfile("该用户没有填写个人简介");
			return new ResponseObject("200", "操作成功", userService.insert(user));
		}
	}

	@GetMapping("/{id:[0-9]+}")
	@ApiOperation("查询User")
	public ResponseObject getById(@PathVariable Integer id) {
		log.info("查询User");
		return new ResponseObject("200", "操作成功", userService.selectById(id));
	}

	@PutMapping
	@ApiOperation("修改User")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject put(User user) {
		log.info("修改User");
		if (user.getNickname() == null || user.getNickname().equals("")) {
			throw new ControllerException("nickname不可为null，也不可为空字符串");
		} else if (user.getProfile() == null || user.getProfile().equals("")) {
			throw new ControllerException("profile不可为null，也不可为空字符串");
		} else if (user.getPhone() == null || user.getPhone().equals("")) {
			throw new ControllerException("phone不可为null，也不可为空字符串");
		} else if (user.getEmail() == null || user.getEmail().equals("")) {
			throw new ControllerException("email不可为null，也不可为空字符串");
		} else {
			User user2 = userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			user2.setNickname(user.getNickname());
			user2.setProfile(user.getProfile());
			user2.setPhone(user.getPhone());
			user2.setEmail(user.getEmail());
			return new ResponseObject("200", "操作成功", userService.update(user2));
		}
	}

}
