package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Volume;

public interface VolumeRepository extends JpaRepository<Volume, Integer> {
	@Query(value = "select * from volume where novel_id = :novel_id", nativeQuery = true)
	List<Volume> selectByNovel_id(@Param(value = "novel_id") Integer novel_id);
	
	@Query(value = "select * from volume where novel_id = :novel_id and name=:name", nativeQuery = true)
	Volume selectByNovel_idName(@Param(value = "novel_id") Integer novel_id, @Param(value = "name") String name);
}
