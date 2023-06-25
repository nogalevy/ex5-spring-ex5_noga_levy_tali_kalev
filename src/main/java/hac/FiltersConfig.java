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
        //Interceptor for unauth users
        registry.addInterceptor(new UnauthInterceptor(sessionUser)).addPathPatterns("/users/**");
        //Interceptor for auth users (logged in)
        registry.addInterceptor(new AuthInterceptor(sessionUser)).addPathPatterns("/pages/**", "/");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}