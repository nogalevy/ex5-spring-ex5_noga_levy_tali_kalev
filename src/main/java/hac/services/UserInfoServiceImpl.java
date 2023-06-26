package hac.services;

import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements  UserInfoService{

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * finds user by email and password
     * @param email string
     * @param password string
     * @return id of user if found
     * @throws Exception if user not found
     */
    @Override
    public Long findUser(String email, String password) throws Exception {
        UserInfo existingUser = userInfoRepository.findUserByEmail(email);
        if(existingUser == null || !passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new Exception("Invalid email or password");
        }
        return existingUser.getId();
    }

    /**
     * registers user
     * @param userInfo object
     * @throws Exception if user already exists
     */
    @Override
    public void registerUser(UserInfo userInfo) throws Exception {
        UserInfo existingUser = userInfoRepository.findUserByEmail(userInfo.getEmail());
        if(existingUser != null){
            throw new Exception("There is already an account registered with that email");
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
    }

    /**
     * gets user by id
     * @param id long
     * @return user object
     * @throws Exception if user not found
     */
    @Override
    public UserInfo getUserById(Long id) throws Exception {
        UserInfo existingUser = userInfoRepository.findById(id).orElse(null);
        if(existingUser == null){
            throw new Exception("User does not exist");
        }
        return existingUser;
    }
}
