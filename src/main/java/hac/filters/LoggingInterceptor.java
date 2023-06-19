package hac.filters;

import org.springframework.web.servlet.HandlerInterceptor;
import hac.beans.UserSession;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingInterceptor implements HandlerInterceptor {

    private UserSession userSession;
//    public LoggingInterceptor() {}

    // let's say we want access to some bean, a solution is to pass the bean to the ctor
    // or define some setter method to pass it
    public LoggingInterceptor(UserSession u) {
        userSession = u;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestPath = request.getRequestURI();
        System.out.println("Request path: " + requestPath);
        if (requestPath.startsWith("/static/")) {
            return true;
        }
        System.out.println("Session bean in filter: " + userSession.isLoggedIn());

        // filter can redirect response to a specific page
        // response.sendRedirect("/error");

        // return FALSE will block the request chaining!

        return true; // continue with the request to next filter or to controller
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, //
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, //
                                Object handler, Exception ex) throws Exception {

    }

}
