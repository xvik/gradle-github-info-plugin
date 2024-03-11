package ru.vyarus.gradle.plugin.github.configurer

import groovy.transform.CompileStatic
import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.GithubInfoExtension
import ru.vyarus.gradle.plugin.github.helper.ExtensionModel

/**
 * Configurer implementation configures exact plugin with info from {@link GithubInfoExtension} (passed
 * {@link ExtensionModel} object is a static clone of extension, required for configuration cache support).
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
@CompileStatic
interface GithubInfoConfigurer {

    /**
     * Called after project evaluation to check if plugin registered and configure it.
     *
     * @param project project instance
     * @param github extension instance
     */
    void configure(Project project, ExtensionModel github)
}
