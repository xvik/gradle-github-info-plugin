package ru.vyarus.gradle.plugin.github.configurer

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.gradle.api.Project
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
                pluginBundle.with {
                    website = website ?: github.site
                    vcsUrl = vcsUrl ?: github.vcsUrl
                }
            }
        }
    }
}
