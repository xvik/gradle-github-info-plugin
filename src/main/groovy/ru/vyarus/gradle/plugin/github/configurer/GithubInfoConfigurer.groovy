package ru.vyarus.gradle.plugin.github.configurer

import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * Configurer implementation configures exact plugin with info from {@link GithubInfoExtension}.
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
interface GithubInfoConfigurer {

    /**
     * Called after project evaluation to check if plugin registered and configure it.
     *
     * @param project project instance
     * @param github extension instance
     */
    void configure(Project project, GithubInfoExtension github)
}
