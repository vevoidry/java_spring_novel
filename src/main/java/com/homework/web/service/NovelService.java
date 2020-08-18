package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Novel;
import com.homework.web.repository.NovelRepository;

@Service
public class NovelService {

	@Autowired
	NovelRepository novelRepository;

	public Novel insert(Novel novel) {
		return novelRepository.save(novel);
	}

	public Novel update(Novel novel) {
		return novelRepository.save(novel);
	}

	public List<Novel> selectByCategory_id(Integer category_id) {
		return novelRepository.selectByCategory_id(category_id);
	}

	public Novel selectById(Integer id) {
		return novelRepository.findById(id).get();
	}

	public List<Novel> selectByRank(String day, Integer limit_count) {
		return novelRepository.selectByRank(day, limit_count);
	}

	public List<Novel> selectByUser_id(Integer user_id) {
		return novelRepository.selectByUser_id(user_id);
	}

	public List<Novel> selectByLikeName(String name) {
		return novelRepository.selectByLikeName(name);
	}

	public List<Novel> selectByUser_idOfCollection(Integer user_id) {
		return novelRepository.selectByUser_idOfCollection(user_id);
	}

	public List<Novel> selectByUser_idOfRecommend(Integer user_id) {
		return novelRepository.selectByUser_idOfRecommend(user_id);
	}
}
