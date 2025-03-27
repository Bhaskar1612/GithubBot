package com.example.githubbot.service;

import org.kohsuke.github.GHPullRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_RETRY_DELAY = 1000; // 1 second

    private final GithubBotService botService;

    public SchedulerService(GithubBotService botService) {
        this.botService = botService;
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void checkOpenPRs() {
        int retryCount = 0;
        while (retryCount <= MAX_RETRIES) {
            try {
                List<GHPullRequest> prs = botService.fetchNewPRs();
                for (GHPullRequest pr : prs) {
                    botService.runStaticAnalysis(pr);
                }
                break; // Success, exit retry loop
            } catch (IOException e) {
                retryCount++;
                if (retryCount > MAX_RETRIES) {
                    logger.error("Failed to check PRs after " + MAX_RETRIES + " attempts", e);
                    break;
                }
                try {
                    long delay = INITIAL_RETRY_DELAY * (long) Math.pow(2, retryCount - 1);
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("Retry interrupted", ie);
                    break;
                }
            }
        }
    }
}
