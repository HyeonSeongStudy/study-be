package project.studyproject.domain.admin.repository;

import org.aspectj.lang.annotation.After;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.admin.entity.AfterEntity;

@Repository
public interface AfterRepository extends JpaRepository<AfterEntity, Long> {
}
