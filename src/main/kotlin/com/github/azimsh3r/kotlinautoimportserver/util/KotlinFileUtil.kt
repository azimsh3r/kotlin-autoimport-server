package com.github.azimsh3r.kotlinautoimportserver.util

import com.github.azimsh3r.kotlinautoimportserver.model.Project
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.vfs.CharsetToolkit
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile

object KotlinFileUtil {

    fun getFilesFrom(project: Project, coreEnvironment: KotlinCoreEnvironment) = project.files.map {
        (PsiFileFactory.getInstance(coreEnvironment.project) as PsiFileFactoryImpl)
            .trySetupPsiForFile(
                LightVirtualFile(
                    if (it.name.endsWith(".kt")) it.name else "${it.name}.kt",
                    KotlinLanguage.INSTANCE,
                    it.text
                ).apply { charset = CharsetToolkit.UTF8_CHARSET },
                KotlinLanguage.INSTANCE, true, false
            ) as KtFile
    }
}