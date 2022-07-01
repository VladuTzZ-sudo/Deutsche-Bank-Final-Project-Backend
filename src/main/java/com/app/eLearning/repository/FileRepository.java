package com.app.eLearning.repository;

import com.app.eLearning.dao.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer>
{
	List<File> findByCourseId(int courseId);
	File findFirstByName(String name);
}
