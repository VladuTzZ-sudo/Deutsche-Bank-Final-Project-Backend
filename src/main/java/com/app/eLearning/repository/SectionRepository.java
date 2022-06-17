package com.app.eLearning.repository;

import com.app.eLearning.dao.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Integer>
{
    public Section findFirstById(int sectionId);
}
