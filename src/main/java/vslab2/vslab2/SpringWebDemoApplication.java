package vslab2.vslab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
public class SpringWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebDemoApplication.class, args);
    }
}
