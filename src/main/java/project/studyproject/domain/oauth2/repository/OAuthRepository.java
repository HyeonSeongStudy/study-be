package project.studyproject.domain.oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.oauth2.entity.OAuthUser;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthUser, Long> {

    OAuthUser getByUsername(String username);

}
