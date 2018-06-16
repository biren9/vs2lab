package vslab2.vslab2.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bitter.db")
public class DBproperties {
    private String hostname = "localhost";

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
