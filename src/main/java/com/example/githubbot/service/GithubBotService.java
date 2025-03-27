package com.example.githubbot.service;

import com.example.githubbot.config.BotConfig;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHIssueState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GithubBotService {

    private static final Logger logger = LoggerFactory.getLogger(GithubBotService.class);
    private final GitHub github;
    private final BotConfig botConfig;

    public GithubBotService(GitHub github, BotConfig botConfig) {
        this.github = github;
        this.botConfig = botConfig;
    }

    public List<GHPullRequest> fetchNewPRs() throws IOException {
        String repoName = botConfig.getGithubRepo();
        logger.info("Fetching open PRs for repository: {}", repoName);
        GHRepository repository = github.getRepository(repoName);
        return repository.getPullRequests(GHIssueState.OPEN);
    }

    public void runStaticAnalysis(GHPullRequest pullRequest) throws IOException {
        logger.info("Running static analysis for PR: {}", pullRequest.getNumber());
        StaticAnalysisService analysisService = new StaticAnalysisService();
        String analysisResults = analysisService.analyzeCode(pullRequest);
        autoCommentOnPR(pullRequest, formatAnalysisResults(analysisResults));
    }

    public void autoCommentOnPR(GHPullRequest pullRequest, String comment) throws IOException {
        String formattedComment = "### :robot: Code Review Bot Report\n\n" +
            "Here are the issues found in your code:\n\n" +
            formatCommentWithSeverity(comment) +
            "\n\nPlease review these suggestions and let me know if you have any questions!";
        logger.info("Posting comment on PR: {}", pullRequest.getNumber());
        pullRequest.comment(formattedComment);
    }

    private String formatAnalysisResults(String analysisResults) {
        StringBuilder formatted = new StringBuilder();
        String[] issues = analysisResults.split("\n");

        for (String issue : issues) {
            formatted.append("- ").append(issue).append("\n");
        }

        return formatted.toString();
    }

    private String formatCommentWithSeverity(String comment) {
        return comment
            .replace("Potential hardcoded sensitive value detected.", ":warning: **SECURITY** Potential hardcoded sensitive value detected.")
            .replace("High cyclomatic complexity detected", ":red_circle: **COMPLEXITY** High cyclomatic complexity detected")
            .replace("Potential code duplication detected", ":large_orange_diamond: **DUPLICATION** Potential code duplication detected")
            .replace("Insufficient documentation detected", ":large_blue_diamond: **DOCUMENTATION** Insufficient documentation detected");
    }
}
