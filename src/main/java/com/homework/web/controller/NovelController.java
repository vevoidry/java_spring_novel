package com.homework.web.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Novel;
import com.homework.web.pojo.User;
import com.homework.web.service.NovelService;
import com.homework.web.service.UserService;
import com.homework.web.util.MyUtils;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/novel")
@Api(tags = "共同前缀：/api/novel", description = "NovelController")
@Slf4j
public class NovelController {

	@Autowired
	NovelService novelService;
	@Autowired
	UserService userService;

	@PostMapping
	@ApiOperation("新增Novel")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody Novel novel) {
		log.info("新增Novel");
		if (novel.getId() != null) {
			throw new ControllerException("id必须为null");
		} else if (novel.getUser_id() == null) {
			throw new ControllerException("user_id不可为null");
		} else if (novel.getCategory_id() == null) {
			throw new ControllerException("category_id不可为null");
		} else if (novel.getName() == null || novel.getName().equals("")) {
			throw new ControllerException("name不可为null，也不可为空字符串");
		} else if (novel.getSummary() == null || novel.getSummary().equals("")) {
			throw new ControllerException("summary不可为null，也不可为空字符串");
		} else if (novel.getImage() == null || novel.getImage().equals("")) {
			throw new ControllerException("image不可为null，也不可为空字符串");
		} else if (novel.getIs_complete() == null) {
			throw new ControllerException("is_complete不可为null");
		} else {
			novel.setMultiplier(10000);
			novel.setAddend(0);
			return new ResponseObject("200", "操作成功", novelService.insert(novel));
		}
	}

	@GetMapping
	@ApiOperation("查询Novel")
	public ResponseObject get(Integer category_id, Integer user_id, String searchName) {
		log.info("查询Novel");
		if (category_id != null) {
			return new ResponseObject("200", "操作成功", novelService.selectByCategory_id(category_id));
		} else if (user_id != null) {
			return new ResponseObject("200", "操作成功", novelService.selectByUser_id(user_id));
		} else if (searchName != null && !searchName.equals("")) {
			return new ResponseObject("200", "操作成功", novelService.selectByLikeName(searchName));
		} else {
			throw new ControllerException("category_id或user_id或searchName不可为null");
		}
	}

	@GetMapping("/{id:[0-9]+}")
	@ApiOperation("查询Novel")
	public ResponseObject getById(@PathVariable Integer id) {
		log.info("查询Novel");
		if (id != null) {
			return new ResponseObject("200", "操作成功", novelService.selectById(id));
		} else {
			throw new ControllerException("id不可为null");
		}
	}

	@PatchMapping("/{id:[0-9]+}")
	@ApiOperation("修改Novel")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject patchById(@PathVariable Integer id, @RequestBody Novel novel) {
		log.info("修改Novel");
		if (id == null) {
			throw new ControllerException("id不可为null");
		} else {
			Novel novel2 = novelService.selectById(id);
			if (novel2 == null) {
				throw new ControllerException("不存在为该id的novel");
			} else {
				User user = userService.selectByUsername(
						(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				if (user.getId() == novel2.getUser_id() || user.getRole().equals("ADMIN")) {
					if (novel.getName() != null && !novel.getName().equals("")) {
						novel2.setName(novel.getName());
					} else if (novel.getSummary() != null && !novel.getSummary().equals("")) {
						novel2.setSummary(novel.getSummary());
					} else if (novel.getCategory_id() != null) {
						novel2.setCategory_id(novel.getCategory_id());
					} else if (novel.getMultiplier() != null) {
						novel2.setMultiplier(novel.getMultiplier());
					} else if (novel.getAddend() != null) {
						novel2.setAddend(novel.getAddend());
					} else {
						throw new ControllerException("请传入需要修改的数据，如name，summary，category_id，multiplier，addend");
					}
					return new ResponseObject("200", "操作成功", novelService.update(novel2));
				} else {
					throw new ControllerException("该用户无权限修改该小说");
				}
			}
		}
	}

	@GetMapping("/rank")
	@ApiOperation("查询Novel")
	public ResponseObject Rank(Integer day_count, Integer limit_count) {
		if (day_count == null) {
			throw new ControllerException("day_count不可为null");
		} else if (limit_count == null) {
			throw new ControllerException("limit_count不可为null");
		} else {
			Date date = new Date(new Date().getTime() - (long) 1000 * 60 * 60 * 24 * day_count);
			return new ResponseObject("200", "操作成功",
					novelService.selectByRank(MyUtils.simpleDateFormat.format(date), limit_count));
		}
	}

	@PostMapping("/image")
	@ApiOperation("上传image")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject postImage(MultipartFile file) throws Exception {
		log.info("上传image");
		if (file == null) {
			throw new ControllerException("上传的文件不可为null");
		} else {
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			if (!suffix.equals(".png")) {
				throw new RuntimeException("上传的文件必须为png文件");
			} else {
				String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/images/";
				File directory = new File(directoryPath);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				String newFileName = new Date().getTime() + ".png";
				File file2 = new File(directory, newFileName);
				if (!file2.exists()) {
					file2.createNewFile();
				}
				// 保存文件
				file.transferTo(file2);
				return new ResponseObject("200", "操作成功", newFileName);
			}
		}
	}

	@PatchMapping("/image")
	@ApiOperation("修改image")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject patchImage(@RequestBody HashMap<String, String> data) {
		log.info("修改image");
		String image = data.get("image");
		String novel_idString = data.get("novel_id");
		if (image == null || image.equals("")) {
			throw new ControllerException("image不可为null，也不可为空字符串");
		} else if (novel_idString == null || novel_idString.equals("")) {
			throw new ControllerException("novel_id不可为null");
		} else {
			Integer novel_id = Integer.parseInt(novel_idString);
			Novel novel = novelService.selectById(novel_id);
			if (novel == null) {
				throw new ControllerException("根据novel_id查询出来的novel为null");
			} else {
				User user = userService.selectByUsername(
						(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				if (user.getId() == novel.getUser_id() || user.getRole().equals("ADMIN")) {
					novel.setImage(image);
					return new ResponseObject("200", "操作成功", novelService.update(novel));
				} else {
					throw new ControllerException("该用户无权限修改小说头像");
				}
			}
		}
	}

}
