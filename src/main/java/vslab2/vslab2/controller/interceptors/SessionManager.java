package vslab2.vslab2.controller.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionManager extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationService authService;
    private final AuthProperties authProperties;

    public SessionManager(AuthProperties authProperties, AuthenticationService authService) {
        this.authProperties = authProperties;
        this.authService = authService;
        log.info("auth activated: " + authProperties.isAuthEnabled());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authProperties.isAuthEnabled()) {
            return authService.authenticateRequest(request, response, handler);
        }
        return super.preHandle(request, response, handler);
    }
}
