package project.studyproject.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.User.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getByUid(String uid);
}
