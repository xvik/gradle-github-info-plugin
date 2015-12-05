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
        project.bintray.pkg.vcsUrl == "https://github.com/${projectId}.git"
        project.bintray.pkg.issueTrackerUrl == "https://github.com/${projectId}/issues"
        project.bintray.pkg.licenses == ['MIT']
    }

    def "Check user configuration preserved"() {

        when: "plugin configured"
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
                }
            }
        }

        then: "user config preserved"
        def projectId = "test/$project.name"
        project.bintray.pkg.websiteUrl == "http://google.com"
        project.bintray.pkg.vcsUrl == "https://github.com/${projectId}.git"
        project.bintray.pkg.issueTrackerUrl == "https://github.com/${projectId}/issues"
        project.bintray.pkg.licenses == ['OTHER']
    }
}