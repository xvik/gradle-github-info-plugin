package ru.vyarus.gradle.plugin.github.helper

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.AbstractTest

/**
 * @author Vyacheslav Rusakov 
 * @since 02.12.2015
 */
class LicenceHelperTest extends AbstractTest {

    def "Check license parsing"() {
        setup:
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }
        LicenseHelper helper = new LicenseHelper(project)

        expect: "default licenses parsed"
        def mit = helper.find('MIT')
        mit.id == 'MIT'
        mit.name == 'The MIT License'
        mit.url == 'http://opensource.org/licenses/MIT'
    }

    def "Check defaults generation"() {

        setup:
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }
        LicenseHelper helper = new LicenseHelper(project)

        expect: "defaults correct"
        helper.defaultLicenseName(project.github) == 'The MIT License'
        helper.defaultLicenseUrl(project.github) == 'http://opensource.org/licenses/MIT'
    }

    def "Check license detection"() {

        setup:
        file('LICENSE').createNewFile()
        Project project = project {
            apply plugin: "ru.vyarus.github-info"

            github {
                user 'test'
                repository 'sample'
                license 'MIT'
            }
        }
        LicenseHelper helper = new LicenseHelper(project)

        expect: "file recognized"
        helper.defaultLicenseUrl(project.github) == 'https://raw.githubusercontent.com/test/sample/master/LICENSE'
    }
}