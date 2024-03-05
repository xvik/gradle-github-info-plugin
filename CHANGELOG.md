* (BREAKING) Drop gradle 5 and 6 support

### 1.5.0 (2023-02-15)
* (breaking) Starting from gradle 7.6, gradlePlugin.website (and vcsUrl) would be configured instead of pluginBundle.website
  (with publish plugin 1.0 pluginBundle not needed, but trigger backwards compatibility if configured)
   Older gradle versions (before 7.6) would work as before 
* Gradle 8 compatibility

### 1.4.0 (2022-10-08)
* Use HEAD branch for file links by default to support both old default 'master' and new default 'main' (#1)
  - Add 'branch' configuration property (with HEAD as default)

### 1.3.0 (2021-06-30)
* Remove bintray plugin support
* In multi-module projects plugin use root project for defaults (license and changelog files searched in root) 

### 1.2.0 (2020-01-19)
* Remove ".git" suffix for default scm urls  
* (breaking) Drop java 7 support

### 1.1.0 (2016-04-08)
* Add `changelogFile` configuration property, used to specify changelog file path. Default file name is recognized by 
    CHANGELOG.md, CHANGELOG.txt or CHANGELOG files in project root.
* Add bintray plugin properties support (available since 1.6 plugin version):
    - githubRepo (required for bintray github integration and to show github readme)
    - githubReleaseNotesFile (used to show github changelog on bintray)

### 1.0.0 (2015-12-05)
* Initial release