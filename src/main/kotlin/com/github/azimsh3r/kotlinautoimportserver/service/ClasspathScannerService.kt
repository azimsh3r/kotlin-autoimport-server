package com.github.azimsh3r.kotlinautoimportserver.service

import org.springframework.stereotype.Service
import java.io.File
import java.util.jar.JarFile

@Service
class ClasspathScannerService {
    private val excludedPrefixes = listOf("META-INF", "module-info")

    private val classpathClasses: List<String> by lazy {
        loadAllClasspathClasses()
    }

    fun getSuggestionsByPrefix(prefix: String): List<String> {
        return classpathClasses.filter {
            it.substringAfterLast(".").contains(prefix, ignoreCase = true)
        }
    }

    private fun loadAllClasspathClasses(): List<String> {
        val entries = System.getProperty("java.class.path")
            .split(File.pathSeparator)
            .map(::File)

        val classFiles = entries.flatMap { entry ->
            try {
                when {
                    entry.extension == "jar" -> loadClassesFromJar(entry)
                    entry.isDirectory       -> loadClassesFromDirectory(entry)
                    else                    -> emptyList()
                }
            } catch (e: Exception) {
                println("Error processing classpath entry '${entry.path}': ${e.message}")
                emptyList()
            }
        }

        return (classFiles)
            .filterNot { className ->
                excludedPrefixes.any { className.startsWith(it) }
            }
    }

    private fun loadClassesFromJar(file: File): List<String> {
        return JarFile(file).use { jar ->
            jar.entries().asSequence()
                .map { it.name }
                .filter { it.endsWith(".class") && !it.contains("$") }
                .map { it.replace("/", ".").removeSuffix(".class") }
                .toList()
        }
    }

    private fun loadClassesFromDirectory(dir: File): List<String> {
        return dir.walkTopDown()
            .filter { it.isFile && it.extension == "class" && !it.name.contains('$') }
            .map {
                it.relativeTo(dir).invariantSeparatorsPath
                    .removeSuffix(".class")
                    .replace("/", ".")
            }
            .toList()
    }
}
