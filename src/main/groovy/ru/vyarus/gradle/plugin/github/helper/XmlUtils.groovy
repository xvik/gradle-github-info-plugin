package ru.vyarus.gradle.plugin.github.helper

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

/**
 * Xml update utilities.
 *
 * @author Vyacheslav Rusakov
 * @since 11.03.2024
 */
@CompileStatic(TypeCheckingMode.SKIP)
class XmlUtils {

    /**
     * Applies xml node value only if there is no existing value.
     *
     * @param pomXml node
     * @param path target node path (comma-separated)
     * @param value default value to set
     */
    static void appendDefault(Node pomXml, String path, String value) {
        String[] nodes = path.split('\\.')
        Node node = pomXml
        nodes.each {
            node = node[it] ? node[it][0] : node.appendNode(it)
        }
        if (!node.text()) {
            node.value = value
        }
    }
}
