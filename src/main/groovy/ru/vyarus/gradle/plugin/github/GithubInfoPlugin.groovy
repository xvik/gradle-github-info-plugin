package ru.vyarus.gradle.plugin.github

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.configurer.BintrayConfigurer
import ru.vyarus.gradle.plugin.github.configurer.GithubInfoConfigurer
import ru.vyarus.gradle.plugin.github.configurer.PluginPublishConfigurer
import ru.vyarus.gradle.plugin.github.configurer.PomConfigurer

/**
 * Github info plugin generates commonly required links for github hosted project, like site, issues, scm urls.
 * <p>
 * Commonly used licenses are recognized and full license name and url set automatically
 * (see {@link ru.vyarus.gradle.plugin.github.helper.LicenseHelper})
 * Info may be used directly in configurations (e.g. {@code github.site}).
 * <p>
 * Plugin detects and configure the following plugins: maven-publish (pom), plugin-publish and bintray.
 * (see {@link GithubInfoConfigurer} implementations).
 *
 * @author Vyacheslav Rusakov
 * @since 10.11.2015
 * @see GithubInfoExtension
 */
@CompileStatic
class GithubInfoPlugin implements Plugin<Project> {

    private final List<GithubInfoConfigurer> support = new ArrayList<>([
            new PomConfigurer(),
            new PluginPublishConfigurer(),
            new BintrayConfigurer(),
    ])

    @Override
    void apply(Project project) {
        GithubInfoExtension github = project.extensions.create('github', GithubInfoExtension, project)

        project.afterEvaluate {
            validate(github)
            support*.configure(project, github)
        }
    }

    private void validate(GithubInfoExtension github) {
        check(github.user, 'configuration github.user is required')
        check(github.license, 'configuration github.license is required')
        check(github.licenseName, 'configuration github.licenseName is required')
        check(github.licenseUrl,
                'configuration github.licenseUrl is required when LICENSE (or LICENSE.txt) ' +
                        'file not exists in project root')
    }

    private void check(Object condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message)
        }
    }
}
