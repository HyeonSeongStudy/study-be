package project.studyproject.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.User.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 특정 데이터 찾기
    User getByUid(String uid);

    // 이메일 중복 로직
    boolean existsByUid(String uid);
}
