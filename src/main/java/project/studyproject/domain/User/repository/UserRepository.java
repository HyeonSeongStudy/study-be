package project.studyproject.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.User.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 특정 데이터 찾기
    User getByUsername(String username);

    // 이메일 중복 로직
    boolean existsByUsername(String uid);
}
