package vslab2.vslab2.controller.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import vslab2.vslab2.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionManager extends HandlerInterceptorAdapter {
    private static final String SESSION_TOKEN = "sessionToken";

    @Autowired
    AuthenticationService authService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean authEnabled;

    public SessionManager(boolean enabled) {
        authEnabled = enabled;
        log.info("auth activated: " + authEnabled);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (authEnabled) {
            authenticateRequest(request, response, handler);
        }
        return super.preHandle(request, response, handler);
    }

    private void authenticateRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = (String) request.getSession().getAttribute(SESSION_TOKEN);
        if (token == null)
            
    }
}
