package com.homework.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Comment;
import com.homework.web.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository commentRepository;

	public Comment insert(Comment comment) {
		return commentRepository.save(comment);
	}

	public List<Comment> selectByNovel_id(Integer novel_id) {
		return commentRepository.selectByNovel_id(novel_id);
	}

	public List<Comment> selectByUser_id(Integer user_id) {
		return commentRepository.selectByUser_id(user_id);
	}

	public List<Comment> selectByUser_idNovel_id(Integer user_id, Integer novel_id) {
		return commentRepository.selectByUser_idNovel_id(user_id, novel_id);
	}
}
