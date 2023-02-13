package ru.vyarus.gradle.plugin.github.configurer

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.gradle.api.Project
import org.gradle.util.GradleVersion
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * If 'com.gradle.plugin-publish' plugin registered configures urls:
 * <pre>
 *     pluginBundle {
 *         website = github.site
 *         vcsUrl = github.vcsUrl
 *     }
 * </pre>
 * Configuration set only if no value provided yet (user configuration is not overridden).
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
@CompileStatic(TypeCheckingMode.SKIP)
class PluginPublishConfigurer implements GithubInfoConfigurer {

    @Override
    void configure(Project project, GithubInfoExtension github) {
        project.plugins.withId('com.gradle.plugin-publish') {
            project.configure(project) {
                if (GradleVersion.current() < GradleVersion.version('7.6')) {
                    pluginBundle.with {
                        website = website ?: github.site
                        vcsUrl = vcsUrl ?: github.vcsUrl
                    }
                } else {
                    // since gradle 7.6 pluginBundle should not be used
                    gradlePlugin.with {
                        if (!website.present) {
                            website.set(github.site)
                        }
                        if (!vcsUrl.present) {
                            vcsUrl.set(github.vcsUrl)
                        }
                    }
                }
            }
        }
    }
}
