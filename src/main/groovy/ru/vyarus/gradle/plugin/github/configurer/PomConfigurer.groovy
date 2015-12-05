package ru.vyarus.gradle.plugin.github.configurer

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * If 'maven-publish' plugin registered, modifies poms in all publications.
 * <pre>
 *     pom {
 *         url github.site
 *         scm {
 *             url github.vcsUrl
 *             connection github.scmConnection
 *             developerConnection github.scmConnection
 *         }
 *         licenses {
 *             license {
 *                 name github.licenseName
 *                 url github.licenseUrl
 *                 distribution 'repo'
 *             }
 *         }
 *         issueManagement {
 *             system 'GitHub'
 *             url github.issues
 *         }
 *     }
 * </pre>
 * Configuration set only if no value provided yet (user configuration is not overridden).
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
class PomConfigurer implements GithubInfoConfigurer {

    @Override
    void configure(Project project, GithubInfoExtension github) {
        project.plugins.withType(MavenPublishPlugin) {
            PublishingExtension publishing = project.publishing
            // apply to all configured maven publications
            publishing.publications.withType(MavenPublication) {
                pom.withXml {
                    Node pomXml = asNode()
                    appendDefault(pomXml, 'url', github.site)

                    appendDefault(pomXml, 'scm.url', github.vcsUrl)
                    appendDefault(pomXml, 'scm.connection', github.scmConnection)
                    appendDefault(pomXml, 'scm.developerConnection', github.scmConnection)

                    appendDefault(pomXml, 'licenses.license.name', github.licenseName)
                    appendDefault(pomXml, 'licenses.license.url', github.licenseUrl)
                    appendDefault(pomXml, 'licenses.license.distribution', 'repo')

                    appendDefault(pomXml, 'issueManagement.system', 'GitHub')
                    appendDefault(pomXml, 'issueManagement.url', github.issues)
                }
            }
        }

    }

    private void appendDefault(Node pomXml, String name, String value) {
        String[] nodes = name.split('\\.')
        Node node = pomXml
        nodes.each {
            node = node[it] ? node[it][0] : node.appendNode(it)
        }
        if (!node.text()) {
            node.value = value
        }
    }
}
