package hac;


import hac.beans.UserSession;
import hac.filters.AuthInterceptor;
import hac.filters.UnauthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
  this is a class for configuring SringMVC
  here we register our interceptor class and the session listener
  WebMvcConfigurer allows configuring all of the MVC:
 */
@EnableWebMvc
@Configuration
public class FiltersConfig implements WebMvcConfigurer {
    @Resource(name = "sessionUser")
    private UserSession sessionUser;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // if you want to apply filter only for REST controller: change the "/**" pattern

        // if we want to pass some bean to the filter
//        registry.addInterceptor(new LoggingInterceptor(sessionUser)).addPathPatterns("/**").excludePathPatterns("/static/**");
//        registry.addInterceptor(new LoggingInterceptor(sessionUser)).addPathPatterns("/**");

        //Interceptor for unauth users
        registry.addInterceptor(new UnauthInterceptor(sessionUser)).addPathPatterns("/users/**"); //TODO: changes name to unauth
        //Interceptor for auth users (logged in)
        registry.addInterceptor(new AuthInterceptor(sessionUser)).addPathPatterns("/pages/**", "/");

        // no args ctor
//        registry.addInterceptor(new LoggingInterceptor()); //.addPathPatterns("/signup");

        // excluding patterns
        //registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/add-user/**").excludePathPatterns("/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}