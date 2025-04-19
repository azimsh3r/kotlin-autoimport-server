package com.github.azimsh3r.kotlinautoimportserver.service

import com.github.azimsh3r.kotlinautoimportserver.util.UnresolvedRefExtractor
import com.github.azimsh3r.kotlinautoimportserver.component.KotlinEnvironment
import com.github.azimsh3r.kotlinautoimportserver.model.ImportSuggestion
import com.github.azimsh3r.kotlinautoimportserver.util.KotlinFileUtil
import com.github.azimsh3r.kotlinautoimportserver.model.Project
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AutoImportProviderService @Autowired constructor(
    private val kotlinEnvironment: KotlinEnvironment,
    private val unresolvedRefExtractor: UnresolvedRefExtractor,
    private val classpathScannerService: ClasspathScannerService
) {

    fun suggestImports(project: Project): List<ImportSuggestion> {
        return kotlinEnvironment.environment { environment ->
            val files = KotlinFileUtil.getFilesFrom(project, environment)

            val unresolved : List<KtNameReferenceExpression> = unresolvedRefExtractor.extractUnknownReferences(files, environment)

            unresolved.map { reference ->
                val name = reference.getReferencedName()
                val candidates = classpathScannerService.getSuggestionsByPrefix(name)
                ImportSuggestion(name = name, candidates = candidates)
            }
        }
    }
}
