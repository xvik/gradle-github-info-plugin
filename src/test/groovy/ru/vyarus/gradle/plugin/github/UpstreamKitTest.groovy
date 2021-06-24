package ru.vyarus.gradle.plugin.github

/**
 * @author Vyacheslav Rusakov
 * @since 17.01.2020
 */
class UpstreamKitTest extends AbstractKitTest {

    def "Check pom modifications"() {
        setup:
        build """
            plugins {
                id 'java'
                id 'ru.vyarus.github-info'
                id 'ru.vyarus.java-lib' version '$JAVALIB_PLUGIN_VERSION'
            }

            group 'ru.vyarus'
            version 1.0
            description 'sample description'

            github {
                user 'test'
                license 'MIT'
            }

            pom {
                developers {
                    developer {
                        id "dev"
                        name "Dev Dev"
                        email "dev@gmail.com"
                    }
                }
            }

            model {
                tasks.generatePomFileForMavenPublication {
                    destination = file("\$buildDir/generated-pom.xml")
                }
            }
        """
        file('LICENSE').createNewFile()

        when: "run pom task"
        def result = runVer('6.0','generatePomFileForMavenPublication')

        def pomFile = file("build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "declared pom modification applied"
        def developer = pom.developers.developer
        developer.id.text() == 'dev'
        developer.name.text() == 'Dev Dev'
        developer.email.text() == 'dev@gmail.com'

        then: "scm section applied"
        def projectId = "test/$testProjectDir.name"
        def scm = pom.scm
        scm.url.text() == "https://github.com/${projectId}"
        scm.connection.text() == "scm:git:git://github.com/${projectId}"
        scm.developerConnection.text() == "scm:git:git://github.com/${projectId}"

        then: "license section applied"
        def license = pom.licenses.license
        license.name.text() == "The MIT License"
        license.url.text() == "https://raw.githubusercontent.com/$projectId/master/LICENSE"
        license.distribution.text() == "repo"

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"

        then: "defaults applied"
        pom.name.text() == testProjectDir.name
        pom.description.text() == 'sample description'
    }
}
