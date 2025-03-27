package com.example.githubbot.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BotConfig {

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.repo}")
    private String githubRepo;

    @Bean
    public GitHub gitHub() throws IOException {
        return new GitHubBuilder()
            .withOAuthToken(githubToken)
            .build();
    }

    public String getGithubRepo() {
        return githubRepo;
    }
}
