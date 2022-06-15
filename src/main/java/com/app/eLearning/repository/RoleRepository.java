package com.app.eLearning.repository;

import com.app.eLearning.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findFirstById(Integer roleId);

    public Role findFirstByName(String name);
}
