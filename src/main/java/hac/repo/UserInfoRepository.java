package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
//    List<UserInfo> findByUserName(String userName);
    UserInfo findUserByEmail(String email);
}
