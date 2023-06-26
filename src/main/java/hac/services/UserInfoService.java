package hac.services;

import hac.repo.UserInfo;

public interface UserInfoService {

    public Long findUser(String email, String password) throws Exception;

    public void registerUser(UserInfo userInfo)throws Exception;

    public UserInfo getUserById(Long id) throws Exception;
}
