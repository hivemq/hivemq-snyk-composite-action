# HiveMQ Snyk Composite Action

A GitHub composite action that runs `snyk monitor` on a Gradle project and fails instead of uploading an incomplete dependency graph.

## Why

The Snyk CLI resolves Gradle dependencies leniently and uploads whatever graph it could build:

- An unresolved dependency (repository outage, expired credentials, missing SNAPSHOT) is kept without its transitive dependencies.
- A configuration pattern that matches nothing yields an empty graph.

In both cases the scan succeeds, and Snyk reports every issue in the missing part of the graph as resolved.

## How It Works

1. Validates the inputs, finds the Gradle build file in `path` and installs the Snyk CLI
2. Runs `snyk monitor` on that build file with the options from the inputs and passes an init script to the Gradle build that Snyk starts
3. The init script checks the scanned configurations before Snyk builds the dependency graph and fails the build if
   - a dependency or artifact could not be resolved, or
   - no configuration that Snyk scans matches the `configuration-matching` pattern
4. A failed build fails the step, and nothing is uploaded to Snyk

Gradle runs with `--no-scan`, so builds with the Develocity plugin do not try to publish a Build Scan.

## Usage

```yaml
- name: Run Snyk monitor
  uses: hivemq/hivemq-snyk-composite-action@<sha> # v3
  with:
    snyk-token: ${{ secrets.SNYK_TOKEN }}
    org: hivemq-broker
    project-name: hivemq-enterprise
    remote-repo-url: hivemq-enterprise
    project-lifecycle: development
    path: hivemq-enterprise
  env:
    ORG_GRADLE_PROJECT_hivemqCommonsUsername: ${{ secrets.JENKINS_GITHUB_USERNAME }}
    ORG_GRADLE_PROJECT_hivemqCommonsPassword: ${{ secrets.JENKINS_GITHUB_TOKEN }}
```

Environment variables of the step, like the Gradle properties above, are available to Gradle.

The action supports Linux runners only.

## Inputs

| Input                    | Required                         | Default                  | Description                                                                          |
|--------------------------|----------------------------------|--------------------------|--------------------------------------------------------------------------------------|
| `snyk-token`             | Yes                              | -                        | Authentication token for Snyk                                                        |
| `org`                    | Yes                              | -                        | Snyk organization to monitor into                                                    |
| `project-name`           | Yes, unless `sub-project` is set | -                        | Name of the Snyk project                                                             |
| `remote-repo-url`        | Yes                              | -                        | Snyk target that groups the project                                                  |
| `project-lifecycle`      | Yes                              | -                        | Lifecycle of the Snyk project, `development` or `production`                         |
| `target-reference`       | No                               | `${{ github.ref_name }}` | Reference of the project within its target, like a branch or a version               |
| `path`                   | No                               | `.`                      | Directory with the `build.gradle` or `build.gradle.kts` to monitor                   |
| `sub-project`            | No                               | `""`                     | Gradle sub-project to monitor; without `project-name`, Snyk derives the project name |
| `configuration-matching` | No                               | `^runtimeClasspath$`     | Pattern of the Gradle configurations to scan                                         |

Snyk runs with `-d`, so the step log contains its debug output.

## Contributing

For issues or feature requests, please contact the HiveMQ Engineering Platform Team.
