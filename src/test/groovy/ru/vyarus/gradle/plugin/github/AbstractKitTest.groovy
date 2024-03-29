package ru.vyarus.gradle.plugin.github

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

/**
 * @author Vyacheslav Rusakov 
 * @since 18.11.2015
 */
abstract class AbstractKitTest extends Specification {

    static String JAVALIB_PLUGIN_VERSION = '3.0.0'
    static String PLUGIN_PUBLISH_VERSION = '1.2.1'

    boolean debug
    @TempDir File testProjectDir
    File buildFile

    def setup() {
        buildFile = file('build.gradle')
        // jacoco coverage support
        fileFromClasspath('gradle.properties', 'testkit-gradle.properties')
        // override maven local repository
        // (see org.gradle.api.internal.artifacts.mvnsettings.DefaultLocalMavenRepositoryLocator.getLocalMavenRepository)
        System.setProperty("maven.repo.local", new File(testProjectDir, "build/repo").getAbsolutePath());
    }

    def build(String file) {
        buildFile << file
    }

    File file(String path) {
        new File(testProjectDir, path)
    }

    File fileFromClasspath(String toFile, String source) {
        File target = file(toFile)
        target.parentFile.mkdirs()
        target << (getClass().getResourceAsStream(source) ?: getClass().classLoader.getResourceAsStream(source)).text
    }

    /**
     * Enable it and run test with debugger (no manual attach required). Not always enabled to speed up tests during
     * normal execution.
     */
    def debug() {
        debug = true
    }

    String projectName() {
        return testProjectDir.getName()
    }

    GradleRunner gradle(File root, String... commands) {
        GradleRunner.create()
                .withProjectDir(root)
                .withArguments((commands + ['--stacktrace']) as String[])
                .withPluginClasspath()
                .withDebug(debug)
                .forwardOutput()
    }

    GradleRunner gradle(String... commands) {
        gradle(testProjectDir, commands)
    }

    BuildResult run(String... commands) {
        return gradle(commands).build()
    }

    BuildResult runFailed(String... commands) {
        return gradle(commands).buildAndFail()
    }

    BuildResult runVer(String gradleVersion, String... commands) {
        return gradle(commands).withGradleVersion(gradleVersion).build()
    }

    BuildResult runFailedVer(String gradleVersion, String... commands) {
        return gradle(commands).withGradleVersion(gradleVersion).buildAndFail()
    }

    protected String unifyString(String input) {
        return input
                // cleanup win line break for simpler comparisons
                .replace("\r", '')
    }
}
