package vslab2.vslab2.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import vslab2.vslab2.config.properties.DBproperties;

@Configuration
@EnableConfigurationProperties(DBproperties.class)
public class AppConfig {
    private final DBproperties dBproperties;

    @Autowired
    public AppConfig(DBproperties dBproperties) {
        this.dBproperties = dBproperties;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(dBproperties.getHostname(), 6379));
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    @Bean
    Gson gson() {
        return new Gson();
    }
}
