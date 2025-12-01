# HiveMQ Snyk Composite Action

A GitHub composite action that checks for new security vulnerabilities in your project using Snyk, comparing results against a baseline project to identify only new issues.

## Features

- Runs Snyk security scans on your project
- Compares scan results against a baseline project (e.g., main branch)
- Only fails if new issues are introduced
- Generates HTML reports with scan results
- Respects already ignored issues from the baseline project

## How It Works

1. Runs an initial Snyk test to gather project information
2. Queries the Snyk API to find the baseline project (typically from the base branch)
3. Retrieves ignored issues from the baseline project
4. Runs Snyk test again with the ignore policy applied
5. Generates an HTML report of findings
6. Uses `snyk-delta` to compare current results with baseline
7. Fails only if new vulnerabilities are detected

## Usage

### Basic Example

```yaml
name: Snyk Security Check

on:
  pull_request:
    branches: [main]

jobs:
  security-scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Check for new Snyk issues
        uses: hivemq/hivemq-snyk-composite-action@main
        with:
          snyk-token: ${{ secrets.SNYK_TOKEN }}
```

### Advanced Example with All Options

```yaml
- name: Check for new Snyk issues
  uses: hivemq/hivemq-snyk-composite-action@main
  with:
    snyk-token: ${{ secrets.SNYK_TOKEN }}
    snyk-args: '--severity-threshold=high --all-projects'
    artifact-name: 'security-report'
    github-username: ${{ secrets.HIVEMQ_COMMONS_USERNAME }}
    github-token: ${{ secrets.HIVEMQ_COMMONS_TOKEN }}
    enterprise-access-key: ${{ secrets.HIVEMQ_ENTERPRISE_ACCESS_KEY }}
    enterprise-secret-key: ${{ secrets.HIVEMQ_ENTERPRISE_SECRET_KEY }}
    snyk-baseline-project-id: 'your-baseline-project-id'
    snyk-api-version: '2024-09-04'
```

## Inputs

| Input | Required | Default | Description |
|-------|----------|---------|-------------|
| `snyk-token` | Yes | - | Authentication token for Snyk API |
| `snyk-args` | No | `""` | Additional arguments passed to `snyk test` command |
| `artifact-name` | No | `"snyk-report"` | Name of the artifact containing the HTML report and debug logs |
| `github-username` | No | `""` | GitHub username for HiveMQ Commons repository access |
| `github-token` | No | `""` | GitHub token for HiveMQ Commons repository access |
| `enterprise-access-key` | No | `""` | HiveMQ Enterprise access key for enterprise module access |
| `enterprise-secret-key` | No | `""` | HiveMQ Enterprise secret key for enterprise module access |
| `snyk-baseline-project-id` | No | `""` | Explicit baseline project ID (auto-detected if not provided) |
| `snyk-api-version` | No | `"2024-09-04"` | Snyk REST API version to use |

## Outputs

This action uploads an artifact containing:
- `{artifact-name}.html` - Formatted HTML report of Snyk findings
- `snyk-debug.log` - Debug logs from Snyk execution

## Prerequisites

- Your repository must be set up with Snyk monitoring
- A baseline project should exist in Snyk (typically created from your main branch)
- Required secrets must be configured in your GitHub repository settings

## Common Use Cases

### Gradle Projects with HiveMQ Dependencies

For Gradle projects that need access to HiveMQ Commons or Enterprise modules:

```yaml
- uses: hivemq/hivemq-snyk-composite-action@main
  with:
    snyk-token: ${{ secrets.SNYK_TOKEN }}
    github-username: ${{ secrets.HIVEMQ_COMMONS_USERNAME }}
    github-token: ${{ secrets.HIVEMQ_COMMONS_TOKEN }}
    enterprise-access-key: ${{ secrets.HIVEMQ_ENTERPRISE_ACCESS_KEY }}
    enterprise-secret-key: ${{ secrets.HIVEMQ_ENTERPRISE_SECRET_KEY }}
```

### Custom Snyk Arguments

To scan specific severity levels or project configurations:

```yaml
- uses: hivemq/hivemq-snyk-composite-action@main
  with:
    snyk-token: ${{ secrets.SNYK_TOKEN }}
    snyk-args: '--severity-threshold=medium --all-projects --exclude=test/**'
```

## Troubleshooting

### Action Fails with "New issues were found"

This is expected behavior when new security vulnerabilities are introduced. Review the HTML report in the job artifacts to see details about the new issues.

### Baseline Project Not Found

If the baseline project can't be auto-detected, you can specify it explicitly:

```yaml
snyk-baseline-project-id: 'your-project-id-from-snyk'
```

Find your project ID in the Snyk dashboard or API.

### Authentication Errors

Ensure all required tokens and credentials are properly set:
- Verify `SNYK_TOKEN` has appropriate permissions
- Check GitHub tokens have access to private repositories if needed
- Confirm HiveMQ enterprise credentials are valid

## Contributing

For issues or feature requests, please contact the HiveMQ infrastructure team.
