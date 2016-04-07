package ru.vyarus.gradle.plugin.github.helper

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.AbstractTest

/**
 * @author Vyacheslav Rusakov
 * @since 08.04.2016
 */
class ChangelogHelperTest extends AbstractTest {

    def "Check changelog recognition"() {

        setup:
        file('CHANGELOG.md').createNewFile()
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }
        ChangelogHelper helper = new ChangelogHelper(project)

        expect: 'file recognized'
        helper.defaultChangelogName() == 'CHANGELOG.md'
    }

    def "Check file not found"() {

        setup:
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }
        ChangelogHelper helper = new ChangelogHelper(project)

        expect: 'file not found'
        !helper.defaultChangelogName()
    }
}
