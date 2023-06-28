package hac.services;

import hac.exceptions.UserNotFound;
import hac.repo.UserInfo;
import org.apache.catalina.User;

public interface UserInfoService {

    public Long findUser(String email, String password) throws UserNotFound;

    public void registerUser(UserInfo userInfo)throws IllegalArgumentException;

    public UserInfo getUserById(Long id) throws UserNotFound;
}
