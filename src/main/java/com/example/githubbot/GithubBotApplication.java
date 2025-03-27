package com.example.githubbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GithubBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubBotApplication.class, args);
    }
}
