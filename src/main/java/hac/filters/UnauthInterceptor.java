package hac.filters;

import org.springframework.web.servlet.HandlerInterceptor;
import hac.beans.UserSession;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor responsible for authorizing users not logged in to
 * access login and registration pages.
 */
public class UnauthInterceptor implements HandlerInterceptor {

    private UserSession userSession;
    public UnauthInterceptor(UserSession u) {
        userSession = u;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestPath = request.getRequestURI();
        System.out.println("Request path: " + requestPath);

        if(userSession.isLoggedIn()){
            System.out.println("Session bean in filter: " + userSession.isLoggedIn());
            //response.sendRedirect("/");
            //return false;
        }
        return true;
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
