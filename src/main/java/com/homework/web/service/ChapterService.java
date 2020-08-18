package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Chapter;
import com.homework.web.repository.ChapterRepository;

@Service
public class ChapterService {
	@Autowired
	ChapterRepository chapterRepository;

	public Chapter insert(Chapter chapter) {
		return chapterRepository.save(chapter);
	}

	public List<Chapter> selectByVolume_id(Integer volume_id) {
		return chapterRepository.selectByVolume_id(volume_id);
	}

	public Chapter selectById(Integer id) {
		return chapterRepository.findById(id).get();
	}

	public Chapter selectNextByVolume_idId(Integer volume_id, Integer id) {
		return chapterRepository.selectNextByVolume_idId(volume_id, id);
	}

	public Chapter selectLastByVolume_idId(Integer volume_id, Integer id) {
		return chapterRepository.selectLastByVolume_idId(volume_id, id);
	}

}
