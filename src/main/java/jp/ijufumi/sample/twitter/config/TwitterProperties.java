package jp.ijufumi.sample.twitter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.social.twitter")
public class TwitterProperties {
    /**
     * Your application's OAuth App ID.
     */
    private String appId;

    /**
     * Your application's OAuth secret.
     */
    private String appSecret;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
