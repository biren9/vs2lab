package vslab2.vslab2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vslab2.vslab2.controller.interceptors.SessionManager;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class SessionInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthProperties authProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionManager(authProperties.isEnabled()));
    }
}
