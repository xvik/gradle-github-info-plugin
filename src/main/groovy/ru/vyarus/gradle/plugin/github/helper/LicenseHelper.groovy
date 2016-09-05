package ru.vyarus.gradle.plugin.github.helper

import groovy.transform.CompileStatic
import org.gradle.api.Project
import ru.vyarus.gradle.plugin.github.GithubInfoExtension

/**
 * Helps generating default values for {@link GithubInfoExtension}.
 * <p>
 * If LICENSE or LICENSE.txt file exists in project root then {@code licenseUrl} will lead to raw file on github.
 * <p>
 * Contains info for most commonly used licenses. User have to set only short license name and
 * {@code licenseName} will be set to full license name. If license file not found in project then
 * {@code licenseUrl} will be set as link to license page on opensource.org.
 *
 * @author Vyacheslav Rusakov
 * @since 01.12.2015
 */
@CompileStatic
class LicenseHelper {
    private final List<String> licenseFileNames = ['LICENSE', 'LICENSE.txt']

    // @formatter:off
    @SuppressWarnings('DuplicateStringLiteral')
    private final String[][] licenses = [
            ['Apache',        'Apache License 2.0',                           'Apache-2.0'],
            ['GPLv2',         'GNU General Public License 2.0',              'GPL-2.0'],
            ['GPLv3',         'GNU General Public License 3.0',              'GPL-3.0'],
            ['AGPL',          'GNU Affero General Public License 3.0',      'AGPL-3.0'],
            ['LGPLv2.1',      'The GNU Lesser General Public License 2.1', 'LGPL-2.1'],
            ['LGPLv3',        'The GNU Lesser General Public License 3.0', 'LGPL-3.0'],
            ['MIT',            'The MIT License',                              'MIT'],
            ['Artistic',      'Artistic License 2.0',                         'Artistic-2.0'],
            ['EPL',           'Eclipse Public License 1.0',                   'EPL-1.0'],
            ['BSD 3-clause', 'The BSD 3-Clause License',                     'BSD-3-Clause'],
            ['MPL',           'Mozilla Public License 2.0',                   'MPL-2.0'],
    ] as String[][]
    // @formatter:on

    Project project
    Map<String, LicenseDescription> descriptions = [:]

    LicenseHelper(Project project) {
        this.project = project
        licenses.each { String[] lic ->
            descriptions.put(lic[0], new LicenseDescription(
                    id: lic[0], name: lic[1], url: "http://opensource.org/licenses/${lic[2]}"))
        }
    }

    /**
     * @param id license short name
     * @return license description object or null
     */
    LicenseDescription find(String id) {
        descriptions[id]
    }

    /**
     * @param ext extension object
     * @return default license name if license (recognized by {@code license}) or null
     */
    String defaultLicenseName(GithubInfoExtension ext) {
        find(ext.license)?.name
    }

    /**
     * Default url is set either to raw github link to LICENSE or LICENSE.txt file in project root (if exists)
     * or to opensource.org url (if license recognized by {@code license}).
     * @param ext extension object
     * @return default license url or null
     */
    String defaultLicenseUrl(GithubInfoExtension ext) {
        lookupLicenseFile(ext) ?: find(ext.license)?.url
    }

    private String lookupLicenseFile(GithubInfoExtension ext) {
        String file = licenseFileNames.find {
            project.file(it).exists()
        }
        file ? ext.rawFileUrl(file) : null
    }

    /**
     * License description object
     */
    static class LicenseDescription {
        /**
         * Short name
         */
        String id
        /**
         * Full name
         */
        String name
        /**
         * Link to  opensource.org license page
         */
        String url
    }
}
