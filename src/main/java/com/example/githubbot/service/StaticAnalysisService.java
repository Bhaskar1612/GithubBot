package com.example.githubbot.service;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class StaticAnalysisService {

    public String analyzeCode(GHPullRequest pullRequest) throws IOException {
        List<String> issues = new ArrayList<>();

        for (GHPullRequestFileDetail file : pullRequest.listFiles()) {
            String content = file.getPatch();
            issues.addAll(analyzeFileContent(content));
        }

        if (issues.isEmpty()) {
            return "No issues found.";
        } else {
            return String.join("\n", issues);
        }
    }

    private List<String> analyzeFileContent(String content) {
        List<String> issues = new ArrayList<>();

        // Security checks
        if (content.contains("password =") || content.contains("api_key =")) {
            issues.add("Potential hardcoded sensitive value detected.");
        }

        // Complexity analysis
        int complexity = calculateCyclomaticComplexity(content);
        if (complexity > 10) {
            issues.add("High cyclomatic complexity detected: " + complexity + ". Consider refactoring.");
        }

        // Code duplication check
        if (detectCodeDuplication(content)) {
            issues.add("Potential code duplication detected.");
        }

        // Documentation check
        if (!hasSufficientDocumentation(content)) {
            issues.add("Insufficient documentation detected.");
        }

        return issues;
    }

    private int calculateCyclomaticComplexity(String content) {
        int complexity = 1;
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.contains("if ") || line.contains("case ") || line.contains("for ") || 
                line.contains("while ") || line.contains("&&") || line.contains("||")) {
                complexity++;
            }
        }
        return complexity;
    }

    private boolean detectCodeDuplication(String content) {
        // Basic duplication detection by checking for repeated blocks
        String[] lines = content.split("\n");
        Set<String> uniqueLines = new HashSet<>(Arrays.asList(lines));
        return uniqueLines.size() < lines.length * 0.7;
    }

    private boolean hasSufficientDocumentation(String content) {
        // Check for presence of comments and documentation
        String[] lines = content.split("\n");
        int commentCount = 0;

        for (String line : lines) {
            if (line.trim().startsWith("//") || line.trim().startsWith("/*")) {
                commentCount++;
            }
        }

        return commentCount >= lines.length * 0.2;
    }
}
