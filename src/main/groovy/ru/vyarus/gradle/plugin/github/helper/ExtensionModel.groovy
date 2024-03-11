package ru.vyarus.gradle.plugin.github.helper

import groovy.transform.CompileStatic
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * Simplified extension model for configuration cache compatibility.
 *
 * @author Vyacheslav Rusakov
 * @since 09.03.2024
 */
@CompileStatic
class ExtensionModel {
    String user
    String license
    String repository
    String licenseName
    String licenseUrl
    String repositoryUrl
    String issues
    String site
    String vcsUrl
    String scmConnection
    String changelogFile

    static ExtensionModel create(GithubInfoExtension ext) {
        return new ExtensionModel(
                user: ext.user,
                license: ext.license,
                repository: ext.repository,
                licenseName: ext.licenseName,
                licenseUrl: ext.licenseUrl,
                repositoryUrl: ext.repositoryUrl,
                issues: ext.issues,
                site: ext.site,
                vcsUrl: ext.vcsUrl,
                scmConnection: ext.scmConnection,
                changelogFile: ext.changelogFile
        )
    }
}
