# GitHub Bot - Automated Code Review Assistant

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

## Overview
This is a Spring Boot application that automates code review processes on GitHub repositories. It provides automated code analysis, review comments, and reviewer assignment based on code changes.

## Features
- Automated PR detection
- Static code analysis
- Code quality feedback
- Reviewer assignment based on file changes
- Scheduled PR monitoring

## Installation
1. Clone the repository
2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Configure your GitHub token in `application.properties`

## Configuration
Create a `application.properties` file with:
```properties
github.token=your_personal_access_token
github.repo=your-org/your-repo
```

## Running the Application
```bash
mvn spring-boot:run
```

The bot will automatically start monitoring PRs every 5 minutes.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request
