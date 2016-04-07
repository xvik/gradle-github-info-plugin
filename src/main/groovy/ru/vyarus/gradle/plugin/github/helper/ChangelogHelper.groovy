package ru.vyarus.gradle.plugin.github.helper

import org.gradle.api.Project

/**
 * Helps generating default value for {@link ru.vyarus.gradle.plugin.github.GithubInfoExtension}.
 * <p>
 * If CHANGELOG.md, CHANGELOG.txt or CHANGELOG file exists in project root then {@code changelogFile}
 * will be set to this file name.
 *
 * @author Vyacheslav Rusakov
 * @since 08.04.2016
 */
class ChangelogHelper {
    private final List<String> changelogFileNames = ['CHANGELOG.md', 'CHANGELOG.txt', 'CHANGELOG']

    Project project

    ChangelogHelper(Project project) {
        this.project = project
    }

    /**
     * @return changelog file name if changelog file recognized in project root or null
     */
    String defaultChangelogName() {
        changelogFileNames.find {
            project.file(it).exists()
        }
    }
}
