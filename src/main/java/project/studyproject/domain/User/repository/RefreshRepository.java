package project.studyproject.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import project.studyproject.domain.User.entity.RefreshUser;

public interface RefreshRepository extends JpaRepository<RefreshUser, Long> {

    Boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
