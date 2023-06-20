package hac.filters;

import org.springframework.web.servlet.HandlerInterceptor;
import hac.beans.UserSession;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor responsible for authorized users to continue
 * to home, favourites and userprofile pages.
 */
public class AuthInterceptor implements HandlerInterceptor {
    private UserSession userSession;
    public AuthInterceptor(UserSession u) {
        userSession = u;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // filter can redirect response to a specific page
        // response.sendRedirect("/error");

        if(!userSession.isLoggedIn()){
            System.out.println("Session bean in filter: " + userSession.isLoggedIn());

//            response.sendRedirect("/users/login");
//            return false;
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
