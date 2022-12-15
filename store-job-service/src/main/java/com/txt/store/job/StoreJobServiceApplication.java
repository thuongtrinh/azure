package com.txt.store.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
//import org.springframework.data.mongodb.config.EnableMongoAuditing;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableMongoRepositories
@EnableMongoAuditing
public class StoreJobServiceApplication {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
    public static final String SPRING_PROFILE_DEV = "dev";
    public static final String SPRING_PROFILE_UAT = "uat";
    public static final String SPRING_PROFILE_PROD = "prod";
    public static final String SPRING_PROFILE_CLOUD = "cloud";

    private static final Logger log = LoggerFactory.getLogger(StoreJobServiceApplication.class);
    private final Environment env;

    public StoreJobServiceApplication(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        if (activeProfiles.contains(SPRING_PROFILE_DEV) && activeProfiles.contains(SPRING_PROFILE_PROD)) {
            log.error("You have misconfigured your application! It should not run with both the '{}' and '{}' profiles at the same time.", SPRING_PROFILE_DEV, SPRING_PROFILE_PROD);
        }
        if (activeProfiles.contains(SPRING_PROFILE_UAT) && activeProfiles.contains(SPRING_PROFILE_PROD)) {
            log.error("You have misconfigured your application! It should not run with both the '{}' and '{}' profiles at the same time.", SPRING_PROFILE_UAT, SPRING_PROFILE_PROD);
        }
        if (activeProfiles.contains(SPRING_PROFILE_DEV) && activeProfiles.contains(SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not run with both the '{}' and '{}' profiles at the same time.", SPRING_PROFILE_DEV, SPRING_PROFILE_CLOUD);
        }
    }

    // must have a main method spring-boot can run
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StoreJobServiceApplication.class);

        addDefaultProfile(app);

        Environment env = app.run(args).getEnvironment();
        log.info("=================ENV============: {}", env.getProperty("spring.kafka.bootstrap-servers"));

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Protocol: \t\t{}\n\t" +
                        "Profile(s): \t{}" +
                        "\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getActiveProfiles());
    }

    private static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEV);
        app.setDefaultProperties(defProperties);
    }

}