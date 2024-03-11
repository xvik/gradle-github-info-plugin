package ru.vyarus.gradle.plugin.github

import groovy.xml.XmlParser
import org.gradle.testkit.runner.BuildResult

/**
 * @author Vyacheslav Rusakov
 * @since 09.03.2024
 */
class ConfigurationCacheSupportKitTest extends AbstractKitTest {

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

            maven.pom {
                developers {
                    developer {
                        id = "dev"
                        name = "Dev Dev"
                        email = "dev@gmail.com"
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
        BuildResult result = run('--configuration-cache', '--configuration-cache-problems=warn', 'generatePomFileForMavenPublication')

        def pomFile = file("build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "no configuration cache incompatibilities"
        result.output.contains("1 problem was found storing the configuration cache")
        result.output.contains('Gradle runtime: support for using a Java agent with TestKit')
        result.output.contains('Calculating task graph as no cached configuration is available for tasks:')

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
        license.url.text() == "https://raw.githubusercontent.com/$projectId/HEAD/LICENSE"
        license.distribution.text() == "repo"

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"

        then: "defaults applied"
        pom.name.text() == testProjectDir.name
        pom.description.text() == 'sample description'


        when: "run from cache"
        println '\n\n------------------- FROM CACHE ----------------------------------------'
        result = run('--configuration-cache', '--configuration-cache-problems=warn', 'generatePomFileForMavenPublication')
        pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "cache used"
        result.output.contains('Reusing configuration cache.')


        then: "declared pom modification applied"
        with(pom.developers.developer) {
            it.id.text() == 'dev'
            it.name.text() == 'Dev Dev'
            it.email.text() == 'dev@gmail.com'
        }

        then: "scm section applied"
        def projectId2 = "test/$testProjectDir.name"
        with(pom.scm) {
            it.url.text() == "https://github.com/${projectId2}"
            it.connection.text() == "scm:git:git://github.com/${projectId2}"
            it.developerConnection.text() == "scm:git:git://github.com/${projectId2}"
        }

        then: "license section applied"
        with(pom.licenses.license) {
            it.name.text() == "The MIT License"
            it.url.text() == "https://raw.githubusercontent.com/$projectId2/HEAD/LICENSE"
            it.distribution.text() == "repo"
        }

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"

        then: "defaults applied"
        pom.name.text() == testProjectDir.name
        pom.description.text() == 'sample description'
    }


    def "Check plugin-publish compatibility"() {
        setup:
        build """   
            plugins {
                id 'java'                
                id 'ru.vyarus.github-info'
                id 'com.gradle.plugin-publish' version '$PLUGIN_PUBLISH_VERSION'
                id 'ru.vyarus.java-lib' version '$JAVALIB_PLUGIN_VERSION'
            }      

            group 'ru.vyarus'
            version 1.0
            description 'sample description'

            github {
                user 'test'
                license 'MIT'
            }

            maven.pom {
                developers {
                    developer {
                        id = "dev"
                        name = "Dev Dev"
                        email = "dev@gmail.com"
                    }
                }
            }

            model {
                tasks.generatePomFileForPluginMavenPublication {
                    destination = file("\$buildDir/generated-pom.xml")
                }
            }
        """
        file('LICENSE').createNewFile()

        when: "run pom task"
        BuildResult result = run('--configuration-cache', '--configuration-cache-problems=warn', 'generatePomFileForPluginMavenPublication')

        def pomFile = file("build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "no configuration cache incompatibilities"
        result.output.contains("1 problem was found storing the configuration cache")
        result.output.contains('Gradle runtime: support for using a Java agent with TestKit')
        result.output.contains('Calculating task graph as no cached configuration is available for tasks:')

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
        license.url.text() == "https://raw.githubusercontent.com/$projectId/HEAD/LICENSE"
        license.distribution.text() == "repo"

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"

        then: "defaults applied"
        pom.name.text() == testProjectDir.name
        pom.description.text() == 'sample description'


        when: "run from cache"
        println '\n\n------------------- FROM CACHE ----------------------------------------'
        result = run('--configuration-cache', '--configuration-cache-problems=warn', 'generatePomFileForPluginMavenPublication')
        pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "cache used"
        result.output.contains('Reusing configuration cache.')


        then: "declared pom modification applied"
        with(pom.developers.developer) {
            it.id.text() == 'dev'
            it.name.text() == 'Dev Dev'
            it.email.text() == 'dev@gmail.com'
        }

        then: "scm section applied"
        def projectId2 = "test/$testProjectDir.name"
        with(pom.scm) {
            it.url.text() == "https://github.com/${projectId2}"
            it.connection.text() == "scm:git:git://github.com/${projectId2}"
            it.developerConnection.text() == "scm:git:git://github.com/${projectId2}"
        }

        then: "license section applied"
        with(pom.licenses.license) {
            it.name.text() == "The MIT License"
            it.url.text() == "https://raw.githubusercontent.com/$projectId2/HEAD/LICENSE"
            it.distribution.text() == "repo"
        }

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"

        then: "defaults applied"
        pom.name.text() == testProjectDir.name
        pom.description.text() == 'sample description'
    }
}
