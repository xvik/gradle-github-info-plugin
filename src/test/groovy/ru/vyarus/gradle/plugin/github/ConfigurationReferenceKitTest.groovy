package ru.vyarus.gradle.plugin.github

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.TaskOutcome

/**
 * @author Vyacheslav Rusakov 
 * @since 25.11.2015
 */
class ConfigurationReferenceKitTest extends AbstractKitTest {

    def "Check github config visibility in other closures"() {
        setup:
        build """
            plugins {
                id 'java'
                id 'ru.vyarus.github-info'
                id 'com.jfrog.bintray' version '$BINTRAY_PLUGIN_VERSION'
            }

            group 'ru.vyarus'
            version 1.0

            github {
                user 'test'
                license 'MIT'
            }

            bintray {
                publications = ['maven']
                dryRun = true
                publish = false
                pkg {
                    repo = 'xvik'
                    name = project.name
                    desc = project.description
                }
            }

            task checkBintrayConfig {
                doLast {
                    assert bintray.pkg.websiteUrl == github.site
                    assert bintray.pkg.issueTrackerUrl == github.issues
                    assert bintray.pkg.vcsUrl == github.vcsUrl
                    assert bintray.pkg.licenses[0] == github.license
                }
            }
        """

        when: "run validation task"
        BuildResult result = run('checkBintrayConfig')

        then: "declared pom modification applied"
        result.task(':checkBintrayConfig').outcome == TaskOutcome.SUCCESS
    }

    def "Check github config visibility on configuration phase"() {
        setup:
        build """
            plugins {
                id 'java'
                id 'ru.vyarus.github-info'
                id 'com.jfrog.bintray' version '$BINTRAY_PLUGIN_VERSION'
            }

            group 'ru.vyarus'
            version 1.0

            github {
                user 'test'
                license 'MIT'
            }

            bintray {
                pkg {
                    desc = github.licenseName
                }
            }

            task checkBintrayConfig {
                doLast {
                    assert github.licenseName
                    assert bintray.pkg.desc == github.licenseName
                }
            }
        """

        when: "run validation task"
        BuildResult result = run('checkBintrayConfig')

        then: "declared pom modification applied"
        result.task(':checkBintrayConfig').outcome == TaskOutcome.SUCCESS
    }
}
