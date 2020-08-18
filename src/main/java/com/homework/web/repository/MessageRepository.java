package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homework.web.pojo.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
