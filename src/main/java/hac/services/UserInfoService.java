package hac.services;

import hac.exceptions.UserNotFound;
import hac.repo.UserInfo;

/**
 * User info service interface
 */
public interface UserInfoService {

    Long findUser(String email, String password) throws UserNotFound;

    void registerUser(UserInfo userInfo)throws IllegalArgumentException;

    UserInfo getUserById(Long id) throws UserNotFound;
}
