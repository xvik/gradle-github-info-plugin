package ru.vyarus.gradle.plugin.github.configurer

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import ru.vyarus.gradle.plugin.github.helper.ExtensionModel

import static ru.vyarus.gradle.plugin.github.helper.XmlUtils.appendDefault

/**
 * If 'maven-publish' plugin registered, modifies poms in all publications.
 * <pre>
 *     maven.pom {
 *         url = github.site
 *         scm {
 *             url = github.vcsUrl
 *             connection = github.scmConnection
 *             developerConnection = github.scmConnection
 *         }
 *         licenses {
 *             license {
 *                 name = github.licenseName
 *                 url = github.licenseUrl
 *                 distribution = 'repo'
 *             }
 *         }
 *         issueManagement {
 *             system = 'GitHub'
 *             url = github.issues
 *         }
 *     }
 * </pre>
 * Configuration set only if no value provided yet (user configuration is not overridden).
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
@CompileStatic(TypeCheckingMode.SKIP)
class PomConfigurer implements GithubInfoConfigurer {

    @Override
    void configure(Project project, ExtensionModel github) {
        project.plugins.withType(MavenPublishPlugin) {
            PublishingExtension publishing = project.publishing
            // apply to all configured maven publications
            publishing.publications.withType(MavenPublication) {
                pom.withXml {
                    Node pomXml = asNode()
                    // prefix required for configuration cache proper work
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
}
