package ru.vyarus.gradle.plugin.github

import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException
import org.gradle.testfixtures.ProjectBuilder

/**
 * @author Vyacheslav Rusakov 
 * @since 11.11.2015
 */
class GithubInfoPluginTest extends AbstractTest {

    def "Check plugin registration"() {

        when: "activating plugin"
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply "ru.vyarus.github-info"

        then: "configuration registered"
        project.extensions.github

        when: "project without configuration called"
        project.evaluate()

        then: "configuration validation failed"
        def ex = thrown(ProjectConfigurationException)
        ex.getCause() instanceof IllegalStateException
    }

    def "Check valid configuration"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                license 'MIT'
            }
        }

        then: "generated fields valid"
        def projectId = "test/$project.name"
        def github = project.extensions.github;

        github.repository == project.name
        github.repositoryUrl == "https://github.com/$projectId"
        github.site == "https://github.com/$projectId"
        github.issues == "https://github.com/$projectId/issues"
        github.vcsUrl == "https://github.com/${projectId}"
        github.scmConnection == "scm:git:git://github.com/${projectId}"
        github.licenseName == "The MIT License"
        github.licenseUrl == "http://opensource.org/licenses/MIT"
    }

    def "Check unknown license validation"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                license 'TTTT'
            }
        }

        then: "license url required"
        thrown(ProjectConfigurationException)
    }

    def "Check license file detected correctly"() {
        setup:
        testProjectDir.newFile('LICENSE')

        when: "plugin configured"
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                license 'MIT'
            }
        }

        then:
        def projectId = "test/$project.name"
        def github = project.extensions.github;
        github.licenseUrl == "https://raw.githubusercontent.com/$projectId/master/LICENSE"
    }

    def "Check extension object"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                license 'MIT'
            }
        }

        then:
        project.github.user == 'test'
    }

    def "Check remote file reference"() {

        when: "plugin configured"
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }

        then:
        project.github.rawFileUrl('TEST') == 'https://raw.githubusercontent.com/test/sample/master/TEST'
        project.github.rawFileUrl('tt/TEST') == 'https://raw.githubusercontent.com/test/sample/master/tt/TEST'
        project.github.rawFileUrl('tt\\TEST') == 'https://raw.githubusercontent.com/test/sample/master/tt/TEST'
        project.github.rawFileUrl('TEST', 'other') == 'https://raw.githubusercontent.com/test/sample/other/TEST'
    }
}