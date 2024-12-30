package com.ayoub.project_management.Repository;

import com.ayoub.project_management.model.Project;
import com.ayoub.project_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNameContainingIgnoreCaseAndTeamContains(String partialName,User user);
    List<Project> findByTeamContainingOrOwner(User owner, User team);

}
