package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Volume;
import com.homework.web.repository.VolumeRepository;

@Service
public class VolumeService {

	@Autowired
	VolumeRepository volumeRepository;

	public Volume insert(Volume volume) {
		return volumeRepository.save(volume);
	}
	
	public Volume update(Volume volume) {
		return volumeRepository.save(volume);
	}

	public List<Volume> selectByNovel_id(Integer novel_id) {
		return volumeRepository.selectByNovel_id(novel_id);
	}

	public Volume selectByNovel_idName(Integer novel_id, String name) {
		return volumeRepository.selectByNovel_idName(novel_id, name);
	}

	public Volume selectById(Integer id) {
		return volumeRepository.findById(id).get();
	}
	
}
