package ru.vyarus.gradle.plugin.github

import org.gradle.api.Project

/**
 * @author Vyacheslav Rusakov 
 * @since 02.12.2015
 */
class ConfigurationReferenceTest extends AbstractTest {

    def "Check valid configuration"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: 'java'
            apply plugin: 'com.gradle.plugin-publish'
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                license 'MIT'
            }

            gradlePlugin {
                plugins {
                    somePlugin {
                        id = 'ru.vyarus.sample'
                        // any class
                        implementationClass = 'ru.vyarus.gradle.plugin.github.GithubInfoPlugin'
                        description = github.site
                    }
                }
            }
        }

        then: "generated fields valid"
        def projectId = "test/$project.name"

        project.gradlePlugin.plugins.somePlugin.description == "https://github.com/$projectId"
    }
}