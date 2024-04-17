# Gradle Github info plugin
[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat)](http://www.opensource.org/licenses/MIT)
[![CI](https://github.com/xvik/gradle-github-info-plugin/actions/workflows/CI.yml/badge.svg)](https://github.com/xvik/gradle-github-info-plugin/actions/workflows/CI.yml)
[![Appveyor build status](https://ci.appveyor.com/api/projects/status/github/xvik/gradle-github-info-plugin?svg=true)](https://ci.appveyor.com/project/xvik/gradle-github-info-plugin)
[![codecov](https://codecov.io/gh/xvik/gradle-github-info-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/xvik/gradle-github-info-plugin)


### About

Plugin generates common github links (like repository, issues, vcs etc) for project and configures common plugins.
The main intention is to remove boilerplate and simplify project configuration.

Features:

* Supports plugins:
    - `maven-publish` configure published pom sections (including license section)
    - `plugin-publish` configure gradle plugin links
* Conventional github links may be used directly to configure other plugins manually through `github` object (e.g. `github.site`)
* In multi-module project always configures defaults from the root project

You can use it with [java-lib plugin](https://github.com/xvik/gradle-java-lib-plugin) to remove more configuration boilerplate
for java or groovy library or gradle plugin.

##### Summary

* Configuration closures: `github`

### Setup

[![Maven Central](https://img.shields.io/maven-central/v/ru.vyarus/gradle-github-info-plugin.svg)](https://maven-badges.herokuapp.com/maven-central/ru.vyarus/gradle-github-info-plugin)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/ru/vyarus/github-info/ru.vyarus.github-info.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=plugins%20portal)](https://plugins.gradle.org/plugin/ru.vyarus.github-info)

```groovy
buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath 'ru.vyarus:gradle-github-info-plugin:2.0.0'
    }
}
apply plugin: 'ru.vyarus.github-info'
```

OR

```groovy
plugins {
    id 'ru.vyarus.github-info' version '2.0.0'
}
```

#### Compatibility

Plugin compiled for java 8, compatible with java 17

Gradle | Version
--------|-------
7       | 2.0.0
5.1     | [1.5.0](https://github.com/xvik/gradle-github-info-plugin/tree/1.5.0)

#### Snapshots

<details>
      <summary>Snapshots may be used through JitPack</summary>

* Go to [JitPack project page](https://jitpack.io/#ru.vyarus/gradle-github-info-plugin)
* Select `Commits` section and click `Get it` on commit you want to use
  or use `master-SNAPSHOT` to use the most recent snapshot

* Add to `settings.gradle` (top most!) (exact commit hash might be used as version) :

  ```groovy
  pluginManagement {
      resolutionStrategy {
          eachPlugin {
              if (requested.id.id == 'ru.vyarus.github-info') {
                  useModule('ru.vyarus:gradle-github-info-plugin:master-SNAPSHOT')
              }
          }
      }
      repositories {
          gradlePluginPortal()      
          maven { url 'https://jitpack.io' }              
      }
  }    
  ``` 
* Use plugin without declaring version:

  ```groovy
  plugins {
      id 'ru.vyarus.github-info'
  }
  ```  

</details>


### Usage

Minimum required configuration:

```groovy
github {
    user 'test'
    license 'MIT'    
}
```

User may be used to define either user or organization (links will be correct for both).
`github.repository` will be set to project name by default (root project name in case of multi-module).

Other license properties may be also required (see below).

All other properties are generated by conventions. You can override any property.

If some required properties are not set validation error will be thrown to prevent incorrect usage.

#### Using in configurations

All properties may be used in other configurations

```groovy
github {
    user 'test'
    license 'MIT'
}

somePlugin {
    websiteUrl = github.site
    vcsUrl = github.vcsUrl
    importantFileUrl = github.rawFileUrl('IFile.txt')
}
```

### Available properties

| Property      | Description                                      | Default value                                                          |
|---------------|--------------------------------------------------|------------------------------------------------------------------------|
| user          | Github user or organization name                 |                                                                        |
| repository    | Github repository name                           | $rootProject.name                                                      |
| branch        | Branch name for file links                       | HEAD (to support both legacy 'master' and new 'main')                      |
| license       | License short name (e.g. 'MIT')                  |                                                                        |
| licenseName   | License full name (e.g. 'The MIT License')       | may be set by convention (see license section)                         |
| licenseUrl    | Url to license file                              | may be set by convention (see license section)                         |
| repositoryUrl | Github repository url                            | https://github.com/$user/$repository                                   |
| issues        | Url to github issues                             | https://github.com/$user/$repository/issues                            |
| site          | Project website                                  | $repositoryUrl                                                         |
| vcsUrl        | Version control url                              | https://github.com/$user/${repository}                                 |
| scmConnection | SCM connection url                               | scm:git:git://github.com/$user/${repository}                           |
| changelogFile | Path to changelog file, relative to project root | CHANGELOG.md, CHANGELOG.txt or CHANGELOG if file found in project root |

#### License

Plugin contains hardcoded info for most common licenses:

| License ID | License name |
|------------|--------------|
| Apache | [Apache License 2.0](http://opensource.org/licenses/Apache-2.0) |
| GPLv2 | [GNU General Public License 2.0](http://opensource.org/licenses/GPL-2.0) |
| GPLv3 | [GNU General Public License 3.0](http://opensource.org/licenses/GPL-3.0) |
| AGPL | [GNU Affero General Public License 3.0](http://opensource.org/licenses/AGPL-3.0) |
| LGPLv2.1 | [The GNU Lesser General Public License 2.1](http://opensource.org/licenses/LGPL-2.1) |
| LGPLv3 | [The GNU Lesser General Public License 3.0](http://opensource.org/licenses/LGPL-3.0) |
| MIT | [The MIT License](http://opensource.org/licenses/MIT) |
| Artistic | [Artistic License 2.0](http://opensource.org/licenses/Artistic-2.0) |
| EPL | [Eclipse Public License 1.0](http://opensource.org/licenses/EPL-1.0) |
| BSD 3-clause | [The BSD 3-Clause License](http://opensource.org/licenses/BSD-3-Clause) |
| MPL | [Mozilla Public License 2.0](http://opensource.org/licenses/MPL-2.0) |

If `license` property value is one of license id above then `licenseName` will be set.
Otherwise, `licenseName` must be specified manually.

`licenseUrl` default:

* Looks if `LICENSE` or `LICENSE.txt` file contained in project root, then url will be
  `https://raw.githubusercontent.com/$user/$repository/HEAD/LICENSE` (or with txt extension accordingly)
* If license file not found in project, but `license` matches known license id (table above) then url will be set as
  link to `opensource.org` (see links above)
* If neither license file found nor license id recognized then url must be set manually

#### Utility method

`github.rawFileUrl(file, branch)` method may be used in build script to generate direct (raw) urls to files on github repository.

For example,

```groovy
github.rawFileUrl('folder/file.txt')

```

Will generate the following url:

```
https://raw.githubusercontent.com/$user/$repository/HEAD/folder/file.txt
```

Branch parameter is optional ('HEAD' by default in order to support both old 'master' and new 'main' default branch names)

### Plugins defaults

Plugin recognize some common plugins and apply default values to them (but not overrides user configuration!)

#### maven-publish

If [maven-publish](https://docs.gradle.org/current/userguide/publishing_maven.html) plugin available, then
for all defined publications pom will be extended with:

```xml
<url>${github.site}</url>
<scm>
    <url>${github.vcsUrl}</url>
    <connection>${github.scmConnection}</connection>
    <developerConnection>${github.scmConnection}</developerConnection>
</scm>
<licenses>
    <license>
        <name>${github.licenseName}</name>
        <url>%{github.licenseUrl}</url>
        <distribution>repo</distribution>
    </license>
</licenses>
<issueManagement>
    <system>GitHub</system>
    <url>${github.issues}</url>
</issueManagement>
```

#### plugin-publish

If [publish-plugin](https://plugins.gradle.org/docs/publish-plugin) plugin available, then following defaults
will be applied for gradle 7.6 and above:

```groovy
gradlePlugin {
    website = github.site
    vcsUrl = github.vcsUrl
}
```

For older gradle versions `pluginBundle` configured:

```groovy
pluginBundle {
    website = github.site
    vcsUrl = github.vcsUrl
}
```

So you can avoid these properties in `gradlePlugin` configuration in your build file. If you manually specify any of these
values it will not be overridden.

### Might also like

* [quality-plugin](https://github.com/xvik/gradle-quality-plugin) - java and groovy source quality checks
* [pom-plugin](https://github.com/xvik/gradle-pom-plugin) - improves pom generation
* [java-library generator](https://github.com/xvik/generator-lib-java) - java library project generator

---
[![gradle plugin generator](http://img.shields.io/badge/Powered%20by-%20Gradle%20plugin%20generator-green.svg?style=flat-square)](https://github.com/xvik/generator-gradle-plugin) 
