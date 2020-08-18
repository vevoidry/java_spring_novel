package com.homework.web.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homework.exception.ControllerException;
import com.homework.web.pojo.Chapter;
import com.homework.web.service.ChapterService;
import com.homework.web.util.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chapter")
@Api(tags = "共同前缀：/api/chapter", description = "ChapterController")
@Slf4j
public class ChapterController {

	@Autowired
	ChapterService chapterService;

	@PostMapping
	@ApiOperation("新增Chapter")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject post(@RequestBody HashMap<String, String> data) {
		log.info("新增Chapter");
		if (data.get("title") == null || data.get("title").equals("")) {
			throw new ControllerException("volume_id不可为null");
		} else if (data.get("volume_id") == null || data.get("volume_id").equals("")) {
			throw new ControllerException("title不可为null，也不可为空字符串");
		} else if (data.get("chapterContent") == null || data.get("chapterContent").equals("")) {
			throw new ControllerException("chapterContent不可为null，也不可为空字符串");
		} else {
			try {
				// 文件夹
				File directory = new File(ResourceUtils.getURL("src").getPath() + "main/resources/static/txt/");
				if (!directory.exists()) {
					directory.mkdirs();
				}
				// 文件
				String content = new Date().getTime() + ".txt";
				File file2 = new File(directory, content);
				if (!file2.exists()) {
					file2.createNewFile();
				}
				// 往文件内写内容
				FileWriter fileWriter = new FileWriter(file2);
				fileWriter.write(data.get("chapterContent"));
				fileWriter.flush();
				fileWriter.close();
				Chapter chapter = new Chapter();
				chapter.setTitle(data.get("title"));
				chapter.setVolume_id(Integer.parseInt(data.get("volume_id")));
				chapter.setContent(content);
				return new ResponseObject("200", "操作成功", chapterService.insert(chapter));
			} catch (Exception e) {
				throw new ControllerException("操作失败");
			}
		}
	}

	@GetMapping
	@ApiOperation("查询Chapter")
	public ResponseObject get(Integer volume_id) {
		log.info("查询Chapter");
		if (volume_id == null) {
			throw new ControllerException("volume_id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", chapterService.selectByVolume_id(volume_id));
		}
	}

	@GetMapping("/{id:[0-9]+}/last")
	@ApiOperation("查询上一章的Chapter")
	public ResponseObject getLast(@PathVariable Integer id) {
		log.info("查询上一章的Chapter");
		if (id == null) {
			throw new ControllerException("id不可为null");
		} else {
			Chapter chapter = chapterService.selectById(id);
			if (chapter == null) {
				throw new ControllerException("该id无法查找到chapter");
			} else {
				return new ResponseObject("200", "操作成功",
						chapterService.selectLastByVolume_idId(chapter.getVolume_id(), chapter.getId()));
			}
		}
	}

	@GetMapping("/{id:[0-9]+}/next")
	@ApiOperation("查询下一章的Chapter")
	public ResponseObject getNext(@PathVariable Integer id) {
		log.info("查询下一章的Chapter");
		if (id == null) {
			throw new ControllerException("id不可为null");
		} else {
			Chapter chapter = chapterService.selectById(id);
			if (chapter == null) {
				throw new ControllerException("该id无法查找到chapter");
			} else {
				return new ResponseObject("200", "操作成功",
						chapterService.selectNextByVolume_idId(chapter.getVolume_id(), chapter.getId()));
			}
		}
	}

	@GetMapping("/{id:[0-9]+}")
	@ApiOperation("查询Chapter")
	public ResponseObject getById(@PathVariable Integer id) {
		log.info("查询Chapter");
		if (id == null) {
			throw new ControllerException("id不可为null");
		} else {
			return new ResponseObject("200", "操作成功", chapterService.selectById(id));
		}
	}

	@GetMapping("/content")
	@ApiOperation("查询Chapter的content")
	public ResponseObject getContent(String content) {
		log.info("查询Chapter的content");
		if (content == null || content.equals("")) {
			throw new ControllerException("content不可为null，也不可为空字符串");
		} else {
			try {
				StringBuilder stringBuilder = new StringBuilder();
				File file = new File(new File(ResourceUtils.getURL("src").getPath() + "main/resources/static/txt/"),
						content);
				FileReader fileReader = new FileReader(file);
				char[] charArray = new char[1024];
				int length = 0;
				while ((length = fileReader.read(charArray)) != -1) {
					stringBuilder.append(new String(charArray, 0, length));
				}
				fileReader.close();
				return new ResponseObject("200", "操作成功", stringBuilder.toString());
			} catch (Exception e) {
				throw new ControllerException("找不到该章节的内容");
			}
		}
	}

	@PatchMapping("/content")
	@ApiOperation("修改Chapter的content")
	@PreAuthorize("isAuthenticated()")
	public ResponseObject patchContent(String content, @RequestBody HashMap<String, String> data) {
		log.info("修改Chapter的content");
		if (content == null || content.equals("")) {
			throw new ControllerException("content不可为null，也不可为空字符串");
		} else if (data.get("chapterContent") == null || data.get("chapterContent").equals("")) {
			throw new ControllerException("chapterContent不可为null，也不可为空字符串");
		} else {
			try {
				File file = new File(new File(ResourceUtils.getURL("src").getPath() + "main/resources/static/txt/"),
						content);
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(data.get("chapterContent"));
				fileWriter.flush();
				fileWriter.close();
				return new ResponseObject("200", "操作成功", data.get("chapterContent"));
			} catch (Exception e) {
				throw new ControllerException("找不到该章节的内容");
			}
		}
	}

}
