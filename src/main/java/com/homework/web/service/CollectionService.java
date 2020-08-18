package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Collection;
import com.homework.web.repository.CollectionRepository;

@Service
public class CollectionService {
	@Autowired
	CollectionRepository collectionRepository;

	public Collection insert(Collection collection) {
		return collectionRepository.save(collection);
	}

	public Collection selectByUser_idNovel_id(Integer user_id, Integer novel_id) {
		return collectionRepository.selectByUser_idNovel_id(user_id, novel_id);
	}

	public void deleteById(Integer id) {
		collectionRepository.deleteById(id);
	}

	public List<Collection> selectByNovel_id(Integer novel_id) {
		return collectionRepository.selectByNovel_id(novel_id);
	}

	public List<Collection> selectByUser_id(Integer user_id) {
		return collectionRepository.selectByUser_id(user_id);
	}
}
