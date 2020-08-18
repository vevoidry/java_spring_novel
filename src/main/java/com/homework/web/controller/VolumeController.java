package com.homework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.User;
import com.homework.web.pojo.Volume;
import com.homework.web.service.NovelService;
import com.homework.web.service.UserService;
import com.homework.web.service.VolumeService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/volume")
@Api(tags = "共同前缀：/api/volume", description = "VolumeController")
@Slf4j
public class VolumeController {

	@Autowired
	VolumeService volumeService;
	@Autowired
	NovelService novelService;
	@Autowired
	UserService userService;

	@PostMapping
	@ApiOperation("新增Volume")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody Volume volume) {
		log.info("新增Volume");
		if (volume.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (volume.getNovel_id() == null) {
			throw new ControllerException("novel_id不可为null");
		} else if (volume.getName() == null || volume.getName().equals("")) {
			throw new ControllerException("name不可为null，也不可为空字符串");
		} else if (volume.getSummary() == null || volume.getSummary().equals("")) {
			throw new ControllerException("summary不可为null，也不可为空字符串");
		} else if (volumeService.selectByNovel_idName(volume.getNovel_id(), volume.getName()) != null) {
			throw new ControllerException("name不可重复");
		} else {
			User user = userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			if (user.getId() == novelService.selectById(volume.getNovel_id()).getUser_id()
					|| user.getRole().equals("ADMIN")) {
				return new ResponseObject("200", "操作成功", volumeService.insert(volume));
			} else {
				throw new ControllerException("该用户无权限新增该volume");
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Volume")
	public ResponseObject get(Integer novel_id) {
		log.info("查询Volume");
		if (novel_id != null) {
			return new ResponseObject("200", "操作成功", volumeService.selectByNovel_id(novel_id));
		} else {
			throw new ControllerException("novel_id不可为null");
		}
	}

	@GetMapping("/{id:[0-9]+}")
	@ApiOperation("查询Volume")
	public ResponseObject getById(@PathVariable Integer id) {
		log.info("查询Volume");
		if (id != null) {
			return new ResponseObject("200", "操作成功", volumeService.selectById(id));
		} else {
			throw new ControllerException("id不可为null");
		}
	}

	@PatchMapping("/{id:[0-9]+}")
	@ApiOperation("修改Volume")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject patchById(@PathVariable Integer id,@RequestBody Volume volume) {
		log.info("修改Volume");
		if (id == null) {
			throw new ControllerException("id不可为null");
		} else {
			Volume volume2 = volumeService.selectById(id);
			if (volume2 == null) {
				throw new ControllerException("不存在为该id的volume");
			} else if (volume.getName() != null && !volume.getName().equals("")) {
				volume2.setName(volume.getName());
				if (volumeService.selectByNovel_idName(volume2.getNovel_id(), volume2.getName()) != null) {
					throw new ControllerException("name不可重复");
				}
			} else if (volume.getSummary() != null && !volume.getSummary().equals("")) {
				volume2.setSummary(volume.getSummary());
			} else {
				throw new ControllerException("请传入需要修改的数据，如name，summary");
			}
			User user = userService
					.selectByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			if (user.getId() == novelService.selectById(volume2.getNovel_id()).getUser_id()
					|| user.getRole().equals("ADMIN")) {
				return new ResponseObject("200", "操作成功", volumeService.update(volume2));
			} else {
				throw new ControllerException("该用户无权限修改该volume");
			}
		}
	}

}
