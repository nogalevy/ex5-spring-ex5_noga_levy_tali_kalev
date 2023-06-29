package hac.filters;

import org.springframework.web.servlet.HandlerInterceptor;
import hac.beans.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor responsible for authorized users to continue
 * to home, favourites and userprofile pages.
 */
public class AuthInterceptor implements HandlerInterceptor {
    private final UserSession userSession;
    public AuthInterceptor(UserSession u) {
        userSession = u;
    }

    /**
     * Check if user is logged in, if not redirect to login page
     * @param request http request
     * @param response http response
     * @param handler handler
     * @return boolean
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(!userSession.isLoggedIn()){
            response.sendRedirect("/users/login");
            return false;
        }
        return true;
    }
}
