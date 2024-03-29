package ru.vyarus.gradle.plugin.github.configurer

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.AbstractTest

/**
 * @author Vyacheslav Rusakov 
 * @since 02.12.2015
 */
class PluginPublishConfigurerTest extends AbstractTest {

    def "Check configuration applied"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: 'java'
            apply plugin: "ru.vyarus.github-info"
            apply plugin: "com.gradle.plugin-publish"

            github {
                user 'test'
                license 'MIT'
            }
        }

        then: "plugin publish configured"
        def projectId = "test/$project.name"
        project.gradlePlugin.website.get() == "https://github.com/$projectId"
        project.gradlePlugin.vcsUrl.get() == "https://github.com/${projectId}"
    }

    def "Check user configuration preserve"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: 'java'
            apply plugin: "ru.vyarus.github-info"
            apply plugin: "com.gradle.plugin-publish"

            github {
                user 'test'
                license 'MIT'
            }

            gradlePlugin {
                website = 'test'
            }
        }

        then: "user configuration preserved"
        def projectId = "test/$project.name"
        project.gradlePlugin.website.get() == "test"
        project.gradlePlugin.vcsUrl.get() == "https://github.com/${projectId}"
    }

    def "Check configuration from root"() {

        when: "plugin configured"
        Project project = projectBuilder()
                .child('sub') {
                    apply plugin: 'java'
                    apply plugin: "ru.vyarus.github-info"
                    apply plugin: "com.gradle.plugin-publish"

                    github {
                        user 'test'
                        license 'MIT'
                    }
                }.build()

        then: "plugin publish configured"
        def child = project.project(':sub')
        def projectId = "test/$project.name"
        child.gradlePlugin.website.get() == "https://github.com/$projectId"
        child.gradlePlugin.vcsUrl.get() == "https://github.com/${projectId}"
    }

}