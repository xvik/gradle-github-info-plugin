package ru.vyarus.gradle.plugin.github

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

/**
 * @author Vyacheslav Rusakov 
 * @since 03.12.2015
 */
class DependencyFreeKitTest extends AbstractKitTest {

    def "Check plugin works without custom plugins"() {

        setup:
        build """
            plugins {
                id 'java'
                id 'ru.vyarus.github-info'
            }

            group 'ru.vyarus'
            version 1.0

            github {
                user 'test'
                license 'MIT'
            }

            task checkState {
                doLast {
                    assert github.site
                }
            }
        """

        when: "run task without extra plugins in classpath"
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments('checkState', '--stacktrace')
                .withPluginClasspath([new File('build/classes/groovy/main'), new File('build/resources/main')])
                .build()

        then: "no side effects"
        result.task(':checkState').outcome == TaskOutcome.SUCCESS


    }
}