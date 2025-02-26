package project.studyproject.domain.User.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 분리 책임 원칙
 * 유저 엔티티에 리프레쉬 토큰을 저장한다면 DB 관심사가 달라지기 때문에 유지보수 측면에서 리프레쉬 유저에 저장함
 * 다중 토큰 지원
 * 유저 디바이스가 다를 경우 deviceId 컬럼을 별도로 넣어 관리한다.
 * 또한 리프레쉬 토큰은 갑자기 삭제해야하는 경우가 생길 수 있기 때문에 리프레쉬 유저로 놓고 관리한다.
 * 사실상 별도의 테이블에 두는데 실무적으로는 Redis에 저장하여 TTL 이 되기 기다리거나 로그아웃 로직을 구성하여 없애는 경우가 대다수란다.
 * SPA + Rest API 같은 경우
 */
@Entity
@Getter
@Setter
public class RefreshUser {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @Column(unique = true, length = 500)
    private String refreshToken;

    private String expiration;

}
