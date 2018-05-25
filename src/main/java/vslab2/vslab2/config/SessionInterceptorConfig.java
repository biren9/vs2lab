package vslab2.vslab2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.controller.interceptors.SessionManager;
import vslab2.vslab2.service.AuthenticationService;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class SessionInterceptorConfig implements WebMvcConfigurer {

    private final AuthProperties authProperties;

    private final AuthenticationService authService;

    @Autowired
    public SessionInterceptorConfig(AuthProperties authProperties, AuthenticationService authService) {
        this.authProperties = authProperties;
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionManager(authProperties, authService))
                .excludePathPatterns("/", "/login", "/follow/**", "/js/**", "/css/**", "/images/**");
    }

}
