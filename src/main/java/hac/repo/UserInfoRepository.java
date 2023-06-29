package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User info repository saves User's info
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findUserByEmail(String email);
}
