package com.example.githubbot.service;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ReviewerService {

    private static final Map<String, List<String>> FILE_REVIEWER_MAPPING = Map.of(
        ".java", List.of("java-expert", "backend-lead"),
        ".js", List.of("frontend-lead", "javascript-expert"),
        ".py", List.of("data-scientist", "python-expert"),
        ".sql", List.of("database-admin")
    );

    public List<String> determineReviewers(GHPullRequest pullRequest) throws IOException {
        Set<String> reviewers = new HashSet<>();

        for (GHPullRequestFileDetail file : pullRequest.listFiles()) {
            String fileName = file.getFilename();
            String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

            if (FILE_REVIEWER_MAPPING.containsKey(fileExtension)) {
                reviewers.addAll(FILE_REVIEWER_MAPPING.get(fileExtension));
            }
        }

        if (reviewers.isEmpty()) {
            return List.of("default-reviewer");
        }

        return new ArrayList<>(reviewers);
    }
}
