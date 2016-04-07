package ru.vyarus.gradle.plugin.github.configurer

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * If 'com.jfrog.bintray' plugin registered, configures:
 * <pre>
 *     bintray {
 *         pkg {
 *             websiteUrl = github.site
 *             issueTrackerUrl = github.issues
 *             vcsUrl = github.vcsUrl
 *             licenses = [github.license]
 *             githubRepo = "$github.user/$github.repository"
 *             githubReleaseNotesFile = github.changelogFile
 *         }
 *     }
 * </pre>
 * Configuration set only if no value provided yet (user configuration is not overridden).
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
class BintrayConfigurer implements GithubInfoConfigurer {

    @Override
    void configure(Project project, GithubInfoExtension github) {
        project.plugins.withId('com.jfrog.bintray') {
            project.configure(project) {
                bintray.pkg.with {
                    websiteUrl = websiteUrl ?: github.site
                    issueTrackerUrl = issueTrackerUrl ?: github.issues
                    vcsUrl = vcsUrl ?: github.vcsUrl
                    licenses = licenses ?: [github.license]
                    githubRepo = githubRepo ?: "$github.user/$github.repository"
                    githubReleaseNotesFile = githubReleaseNotesFile ?: github.changelogFile
                }
            }
        }
    }
}
