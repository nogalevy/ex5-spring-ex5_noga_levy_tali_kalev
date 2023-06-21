package hac;

import hac.beans.JokesList;
import hac.beans.UserSession;
import hac.beans.SearchFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class BeanConfiguration {
    @Bean
    @ApplicationScope
    public JokesList applicationBeanExample () {
        return new JokesList();
    }

    @Bean
    @SessionScope
    public UserSession sessionUser () {
        return new UserSession();
    }
//
//    @Bean
//    @SessionScope
//    public ShoppingCart sessionBeanCart () {
//        ShoppingCart shpc = new ShoppingCart();
//        return shpc;
//    }
//    @Bean
//    @SessionScope
//    public Messages sessionBeanExample () {
//        return new Messages();
//    }
    @Bean
    @SessionScope
    public SearchFilter searchFilterSession () {
        return new SearchFilter();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
