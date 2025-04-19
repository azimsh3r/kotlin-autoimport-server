package com.github.azimsh3r.kotlinautoimportserver

import com.github.azimsh3r.kotlinautoimportserver.component.KotlinEnvironment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.io.File

@SpringBootApplication
class KotlinAutoimportServerApplication {

    @Bean
    fun kotlinEnvironment(): KotlinEnvironment {
        val classPathString = System.getProperty("java.class.path")

        if (classPathString.isEmpty()) {
            throw IllegalStateException("Classpath is empty. Cannot initialize KotlinEnvironment.")
        }

        val classPath = classPathString
            .split(File.pathSeparator)
            .map(::File)
            .filter { it.exists() }

        if (classPath.isEmpty()) {
            throw IllegalStateException("No valid Kotlin libraries found in the classpath.")
        }

        return KotlinEnvironment(classPath)
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinAutoimportServerApplication>(*args)
}
