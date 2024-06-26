package com.morris.opensquare.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secrets.properties")
public class ApplicationPropertiesConfiguration {

    @Value("${secrets.region}")
    private String region;

    @Value("${secrets.youtube}")
    private String openSentYouTubeSecret;

    @Value("${secrets.userPoolId}")
    private String userPoolId;

    @Value("${secrets.clientId}")
    private String clientId;

    @Value("${secrets.clientSecret}")
    private String clientSecret;

    @Value("${secrets.profilesTable}")
    private String profilesTable;

    @Value("${secrets.dropDownTable}")
    private String dropDownTable;

    @Value("${secrets.userNameIndex}")
    private String userNameIndex;

    @Value("${secrets.googleAppName}")
    private String googleAppName;

    @Value("${secrets.appName}")
    private String appName;

    @Value("${secrets.googleApiKey}")
    private String googleApiKey;

    @Value("${secrets.googleMapsApiKey}")
    private String googleMapsApiKey;

    @Value("${secrets.engineCx}")
    private String engineCx;

    @Value("${secrets.openAI}")
    private String openAI;

    @Value("${secrets.mongodbUri}")
    private String mongodbUri;

    @Value("${secrets.database}")
    private String database;

    @Value("${secrets.youtubeCollection}")
    private String youtubeCollection;

    @Value("${secrets.youtubeVectorIndex}")
    private String youtubeVectorIndex;

    @Bean
    public String region() {
        return this.region;
    }

    @Bean
    public String openSentYouTubeSecret() {
        return this.openSentYouTubeSecret;
    }

    @Bean
    public String userPoolId() {
        return this.userPoolId;
    }

    @Bean
    public String clientId() {
        return this.clientId;
    }

    @Bean
    public String clientSecret() { return this.clientSecret; }

    @Bean
    public String profilesTable() { return this.profilesTable; }

    @Bean
    public String dropDownTable() { return this.dropDownTable; }

    @Bean
    public String userNameIndex() { return this.userNameIndex; }

    @Bean
    public String googleApiKey() { return this.googleApiKey; }

    @Bean
    public String googleMapsApiKey() { return this.googleMapsApiKey; }

    @Bean
    public String googleAppName() { return this.googleAppName; }

    @Bean
    public String engineCx() { return this.engineCx; }

    @Bean
    public String appName() { return this.appName; }

    @Bean
    public String openAI() { return this.openAI; }

    @Bean
    public String mongodbUri() { return this.mongodbUri; }

    @Bean
    public String database() {
        return database;
    }

    @Bean
    public String youtubeCollection() {
        return youtubeCollection;
    }

    @Bean
    public String youtubeVectorIndex() {
        return youtubeVectorIndex;
    }
}
