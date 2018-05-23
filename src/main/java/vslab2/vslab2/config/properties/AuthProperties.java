package vslab2.vslab2.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bitter.auth")
public class AuthProperties {
    private boolean authEnabled;
    private long sessionMinutesDuration;

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public long getSessionMinutesDuration() {
        return sessionMinutesDuration;
    }

    public void setSessionMinutesDuration(long sessionMinutesDuration) {
        this.sessionMinutesDuration = sessionMinutesDuration;
    }
}
