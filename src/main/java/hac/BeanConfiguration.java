package hac;

import hac.beans.JokesList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

@Configuration
public class BeanConfiguration {
    @Bean
    @ApplicationScope
    public JokesList applicationBeanExample () {
        return new JokesList();
    }
}
