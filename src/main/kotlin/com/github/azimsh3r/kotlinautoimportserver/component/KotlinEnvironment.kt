package com.github.azimsh3r.kotlinautoimportserver.component

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.config.configureJdkClasspathRoots
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.js.config.JSConfigurationKeys
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY
import java.io.File

class KotlinEnvironment(
    private val classpath: List<File>
) {
    private val configuration = createConfiguration()
    private val rootDisposable = Disposer.newDisposable()

    private val environment = KotlinCoreEnvironment.createForProduction(
        rootDisposable,
        configuration = configuration.copy(),
        configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
    )

    @Synchronized
    fun <T> environment(f: (KotlinCoreEnvironment) -> T): T {
        return f(environment)
    }

    private fun createConfiguration(): CompilerConfiguration {
        return CompilerConfiguration().apply {
            addJvmClasspathRoots(classpath.filter { it.exists() && it.isFile && it.extension == "jar" })

            put(CommonConfigurationKeys.MODULE_NAME, "web-module")

            val messageCollector = MessageCollector.NONE
            put(MESSAGE_COLLECTOR_KEY, messageCollector)

            put(JSConfigurationKeys.PROPERTY_LAZY_INITIALIZATION, true)

            put(JVMConfigurationKeys.DO_NOT_CLEAR_BINDING_CONTEXT, true)

            configureJdkClasspathRoots()

            val jdkHome = get(JVMConfigurationKeys.JDK_HOME)
            if (jdkHome == null) {
                val javaHome = File(System.getProperty("java.home"))
                put(JVMConfigurationKeys.JDK_HOME, javaHome)
            }
        }
    }
}
