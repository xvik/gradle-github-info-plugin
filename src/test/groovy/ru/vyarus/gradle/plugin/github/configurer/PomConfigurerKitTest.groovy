package ru.vyarus.gradle.plugin.github.configurer

import ru.vyarus.gradle.plugin.github.AbstractKitTest


/**
 * @author Vyacheslav Rusakov 
 * @since 02.12.2015
 */
class PomConfigurerKitTest extends AbstractKitTest {

    def "Check defaults not override user pom values"() {
        setup:
        build """
            plugins {
                id 'java'
                id 'maven-publish'
                id 'ru.vyarus.github-info'
            }

            group 'ru.vyarus'
            version 1.0

            github {
                user 'test'
                license 'MIT'
            }


            publishing {
                publications {
                    maven(MavenPublication) {
                        from components.java
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
        def result = run('generatePomFileForMavenPublication')

        def pomFile = file("build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

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

        then: "issues section applied"
        def issues = pom.issueManagement
        issues.system.text() == "GitHub"
        issues.url.text() == "https://github.com/$projectId/issues"

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"
    }

    def "Check user configuration not overridden"() {
        setup:
        build """
            plugins {
                id 'java'
                id 'maven-publish'
                id 'ru.vyarus.github-info'
            }

            group 'ru.vyarus'
            version 1.0

            github {
                user 'test'
                license 'MIT'
            }


            publishing {
                publications {
                    maven(MavenPublication) {
                        from components.java
                        pom.withXml {
                            asNode().appendNode('url', 'http://google.com')
                            asNode().appendNode('scm').appendNode('url', 'http://other.url')
                        }
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
        def result = run('generatePomFileForMavenPublication')

        def pomFile = file("build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

        then: "scm section merged"
        def projectId = "test/$testProjectDir.name"
        def scm = pom.scm
        scm.url.text() == "http://other.url"
        scm.connection.text() == "scm:git:git://github.com/${projectId}"
        scm.developerConnection.text() == "scm:git:git://github.com/${projectId}"

        then: "license section applied"
        def license = pom.licenses.license
        license.name.text() == "The MIT License"
        license.url.text() == "https://raw.githubusercontent.com/$projectId/master/LICENSE"
        license.distribution.text() == "repo"

        then: "issues section applied"
        def issues = pom.issueManagement
        issues.system.text() == "GitHub"
        issues.url.text() == "https://github.com/$projectId/issues"

        then: "site url not overridden"
        pom.url.text() == "http://google.com"
    }

    def "Check configuration from the root project"() {
        setup:
        build """
            plugins {
                id 'ru.vyarus.github-info' apply false
            }
            
            subprojects {
                apply plugin: 'java'
                apply plugin: 'maven-publish'
                apply plugin: 'ru.vyarus.github-info'

                group 'ru.vyarus'
                version 1.0
    
                github {
                    user 'test'
                    license 'MIT'
                }
    
                publishing {
                    publications {
                        maven(MavenPublication) {
                            from components.java
                        }
                    }
                }
    
                model {
                    tasks.generatePomFileForMavenPublication {
                        destination = file("build/generated-pom.xml")
                    }
                }    
            }
        """
        file('settings.gradle') << "include 'sub'"
        file('LICENSE').createNewFile()

        when: "run pom task"
        def result = run(':sub:generatePomFileForMavenPublication')

        def pomFile = file("sub/build/generated-pom.xml")
        def pom = new XmlParser().parse(pomFile)
        // for debug
        println pomFile.getText()

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

        then: "issues section applied"
        def issues = pom.issueManagement
        issues.system.text() == "GitHub"
        issues.url.text() == "https://github.com/$projectId/issues"

        then: "site url applied"
        pom.url.text() == "https://github.com/$projectId"
    }

}