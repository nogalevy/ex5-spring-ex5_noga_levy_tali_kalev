package hac;

import hac.beans.UserSession;
import hac.beans.SearchFilter;
import hac.services.UserFavouritesServiceImpl;
import hac.services.UserInfoServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class BeanConfiguration {
    @Bean
    @SessionScope
    public UserSession sessionUser () {
        return new UserSession();
    }

    @Bean
    @SessionScope
    public SearchFilter searchFilterSession () {
        return new SearchFilter();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserInfoServiceImpl userInfoService() {
        return new UserInfoServiceImpl();
    }

    @Bean
    public UserFavouritesServiceImpl userFavouritesService() {
        return new UserFavouritesServiceImpl();
    }
}
