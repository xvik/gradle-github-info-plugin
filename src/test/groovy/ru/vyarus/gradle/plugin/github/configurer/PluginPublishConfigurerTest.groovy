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
        project.pluginBundle.website == "https://github.com/$projectId"
        project.pluginBundle.vcsUrl == "https://github.com/${projectId}"
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

            pluginBundle {
                website = 'test'
            }
        }

        then: "user configuration preserved"
        def projectId = "test/$project.name"
        project.pluginBundle.website == "test"
        project.pluginBundle.vcsUrl == "https://github.com/${projectId}"
    }

}