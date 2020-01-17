package ru.vyarus.gradle.plugin.github.configurer

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.AbstractTest

/**
 * @author Vyacheslav Rusakov 
 * @since 02.12.2015
 */
class BintrayConfigurerTest extends AbstractTest {

    def "Check configuration applied"() {

        when: "plugin configured"
        file('CHANGELOG.md').createNewFile()
        Project project = project {
            apply plugin: 'java'
            apply plugin: 'ru.vyarus.github-info'
            apply plugin: 'com.jfrog.bintray'

            github {
                user 'test'
                license 'MIT'
            }
        }

        then: "bintray plugin configured"
        def projectId = "test/$project.name"
        project.bintray.pkg.websiteUrl == "https://github.com/$projectId"
        project.bintray.pkg.vcsUrl == "https://github.com/${projectId}"
        project.bintray.pkg.issueTrackerUrl == "https://github.com/${projectId}/issues"
        project.bintray.pkg.licenses == ['MIT']
        project.bintray.pkg.githubRepo == projectId
        project.bintray.pkg.githubReleaseNotesFile == 'CHANGELOG.md'
    }

    def "Check user configuration preserved"() {

        when: "plugin configured"
        file('CHANGELOG.md').createNewFile()
        Project project = project {
            apply plugin: 'java'
            apply plugin: 'ru.vyarus.github-info'
            apply plugin: 'com.jfrog.bintray'

            github {
                user 'test'
                license 'MIT'
            }

            bintray {
                pkg {
                    websiteUrl = 'http://google.com'
                    licenses = ['OTHER']
                    githubReleaseNotesFile = 'FOO.txt'
                }
            }
        }

        then: "user config preserved"
        def projectId = "test/$project.name"
        project.bintray.pkg.websiteUrl == "http://google.com"
        project.bintray.pkg.vcsUrl == "https://github.com/${projectId}"
        project.bintray.pkg.issueTrackerUrl == "https://github.com/${projectId}/issues"
        project.bintray.pkg.licenses == ['OTHER']
        project.bintray.pkg.githubRepo == projectId
        project.bintray.pkg.githubReleaseNotesFile == 'FOO.txt'
    }

    def "Changelog override test"() {

        when: "plugin configured"
        file('CHANGELOG.md').createNewFile()
        Project project = project {
            apply plugin: 'java'
            apply plugin: 'ru.vyarus.github-info'
            apply plugin: 'com.jfrog.bintray'

            github {
                user 'test'
                license 'MIT'
                changelogFile = 'FOO.txt'
            }
        }

        then: "user config preserved"
        project.bintray.pkg.githubReleaseNotesFile == 'FOO.txt'
    }
}