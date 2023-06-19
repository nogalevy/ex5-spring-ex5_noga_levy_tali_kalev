package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
//    List<UserInfo> findByUserName(String userName);
}
