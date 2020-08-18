package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Recommend;
import com.homework.web.repository.RecommendRepository;

@Service
public class RecommendService {

	@Autowired
	RecommendRepository recommendRepository;

	public Recommend insert(Recommend recommend) {
		return recommendRepository.save(recommend);
	}

	public Recommend selectByUser_idNovel_id(Integer user_id, Integer novel_id) {
		return recommendRepository.selectByUser_idNovel_id(user_id, novel_id);
	}

	public void deleteById(Integer id) {
		recommendRepository.deleteById(id);
	}

	public List<Recommend> selectByNovel_id(Integer novel_id) {
		return recommendRepository.selectByNovel_id(novel_id);
	}

}
