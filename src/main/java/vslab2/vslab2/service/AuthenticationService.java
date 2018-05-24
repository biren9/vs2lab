package vslab2.vslab2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.dbLayer.BitterDB;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final BitterDB dao;
    private final AuthProperties authProperties;
    private final static String SESSION_TOKEN = "sessionToken";
    private final static String CLIENT_USERNAME = "client_username";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AuthenticationService(BitterDB dao, AuthProperties authProperties) {
        this.dao = dao;
        this.authProperties = authProperties;
    }

    public boolean handleLoginRequest(HttpServletRequest request, HttpServletResponse response, String username, String password) {
        if (password.equals(dao.getUserPassword(username))) {
            String uuid = UUID.randomUUID().toString();
            dao.saveSession(username, authProperties.getSessionMinutesDuration(), uuid);
            response.addCookie(new Cookie(SESSION_TOKEN, uuid));
            response.addCookie(new Cookie(CLIENT_USERNAME, username));
            return true;
        }
        return false;
    }

    public boolean authenticateRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = null;
        String clientUsername = null;
        if (request.getCookies() == null) {
            return redirectToLogin(response);
        }
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(SESSION_TOKEN)) {
                token = c.getValue();
            }
            if (c.getName().equals(CLIENT_USERNAME)) {
                clientUsername = c.getValue();
            }
        }

        if (token == null || clientUsername == null || token.isEmpty() || clientUsername.isEmpty()) {
            return redirectToLogin(response);
        }
        String user = dao.getUserBySessionToken(token);
        if (user == null) {
            return redirectToLogin(response);
        }
        return user.equals(clientUsername);
    }

    public String getAuthenticatedUserByRequest(HttpServletRequest request) {
        String token = null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(SESSION_TOKEN)) {
                token = c.getValue();
            }
        }
        return dao.getUserBySessionToken(token);
    }

    private boolean redirectToLogin(HttpServletResponse response) {
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            log.warn(e.toString());
        }
        return false;
    }

    public void addUserToModel(HttpServletRequest req, ModelAndView modelAndView) {
        String token = null;
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(SESSION_TOKEN)) {
                token = c.getValue();
            }
        }
        modelAndView.addObject("username", dao.getUserBySessionToken(token));
    }
}
