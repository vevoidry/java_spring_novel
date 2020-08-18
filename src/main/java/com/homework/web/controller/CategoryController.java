package com.homework.web.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Category;
import com.homework.web.service.CategoryService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/category")
@Api(tags = "共同前缀：/api/category", description = "CategoryController")
@Slf4j
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PostMapping
	@ApiOperation("新增Category")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseObject post(@RequestBody Category category) {
		log.info("新增Category");
		if (category.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (category.getRank() == null) {
			throw new ControllerException("rank不可为null");
		} else if (category.getName() == null || category.getName().equals("")) {
			throw new ControllerException("name不可为null，也不可为空字符串");
		} else if (category.getParent_id() == null) {
			throw new ControllerException("parent_id不可为null");
		} else if (categoryService.selectByParent_idName(category.getParent_id(), category.getName()) != null) {
			throw new ControllerException("name不可重复");
		} else {
			return new ResponseObject("200", "操作成功", categoryService.insert(category));
		}
	}

	@PutMapping("/{id:[0-9]+}")
	@ApiOperation("修改Category")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseObject putById(@PathVariable Integer id, @RequestBody HashMap<String, String> data) {
		log.info("修改Category");
		Category category = categoryService.selectById(id);
		if (category == null) {
			throw new ControllerException("使用该id的Category不存在");
		} else if (categoryService.selectByParent_idName(category.getParent_id(), data.get("name")) != null) {
			throw new ControllerException("同一个分类下的name不可重复");
		} else {
			category.setName(data.get("name"));
			return new ResponseObject("200", "操作成功", categoryService.update(category));
		}
	}

	@GetMapping
	@ApiOperation("查询Category")
	public ResponseObject getByParent_id(Integer rank, Integer parent_id) {
		log.info("查询Category");
		if (rank != null && parent_id != null) {
			return new ResponseObject("200", "操作成功", categoryService.selectByRankParent_id(rank, parent_id));
		} else if (rank != null && parent_id == null) {
			return new ResponseObject("200", "操作成功", categoryService.selectByRank(rank));
		} else if (rank == null && parent_id != null) {
			return new ResponseObject("200", "操作成功", categoryService.selectByParent_id(parent_id));
		} else {
			throw new ControllerException("rank或者parent_id至少要有一个不为null");
		}
	}

	@GetMapping("/{id:[0-9]+}")
	@ApiOperation("查询Category")
	public ResponseObject getById(@PathVariable Integer id) {
		log.info("查询Category");
		if (id != null) {
			return new ResponseObject("200", "操作成功", categoryService.selectById(id));
		} else {
			throw new ControllerException("id不可为null");
		}
	}

}
