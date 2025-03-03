package project.studyproject.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.admin.entity.BeforeEntity;

@Repository
public interface BeforeRepository extends JpaRepository<BeforeEntity, Long> {
}
