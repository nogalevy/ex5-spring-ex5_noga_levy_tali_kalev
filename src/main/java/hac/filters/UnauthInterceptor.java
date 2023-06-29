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

    private final UserSession userSession;
    public UnauthInterceptor(UserSession u) {
        userSession = u;
    }

    /**
     * Check if user is logged in, if so redirect to home page else continues to login or register page
     * @param request http request
     * @param response http response
     * @param handler handler
     * @return boolean
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if(userSession.isLoggedIn()){
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
