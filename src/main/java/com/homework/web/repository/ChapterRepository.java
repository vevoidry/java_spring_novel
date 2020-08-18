package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
	@Query(value = "select * from chapter where volume_id = :volume_id", nativeQuery = true)
	List<Chapter> selectByVolume_id(@Param(value = "volume_id") Integer volume_id);

	@Query(value = "SELECT * FROM chapter WHERE id > :id AND volume_id = :volume_id ORDER BY id ASC limit 1", nativeQuery = true)
	Chapter selectNextByVolume_idId(@Param(value = "volume_id") Integer volume_id, @Param(value = "id") Integer id);

	@Query(value = "SELECT * FROM chapter WHERE id < :id AND volume_id = :volume_id ORDER BY id DESC limit 1", nativeQuery = true)
	Chapter selectLastByVolume_idId(@Param(value = "volume_id") Integer volume_id, @Param(value = "id") Integer id);
}
