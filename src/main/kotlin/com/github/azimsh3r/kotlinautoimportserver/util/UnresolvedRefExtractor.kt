package com.github.azimsh3r.kotlinautoimportserver.util

import com.github.azimsh3r.kotlinautoimportserver.model.ImportSuggestion
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.jvm.compiler.CliBindingTrace
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.container.getService
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.LazyTopDownAnalyzer
import org.jetbrains.kotlin.resolve.TopDownAnalysisMode
import org.jetbrains.kotlin.resolve.lazy.declarations.FileBasedDeclarationProviderFactory
import org.springframework.stereotype.Component

//TODO: add support for handling multiple files
@Component
class UnresolvedRefExtractor {

    fun extractUnknownReferences(files: List<KtFile>, environment: KotlinCoreEnvironment) : List<KtNameReferenceExpression> {
        val bindingContext = analyzeFile(files, environment)
        return extractUnresolvedReferences(files.first(), bindingContext)
    }

    private fun analyzeFile(files: List<KtFile>, environment: KotlinCoreEnvironment): BindingContext{
        val trace = CliBindingTrace()
        val project = files.first().project

        val container = TopDownAnalyzerFacadeForJVM.createContainer(
            project = project,
            files = files,
            trace = trace,
            configuration = environment.configuration,
            packagePartProvider = { scope -> environment.createPackagePartProvider(scope) },
            declarationProviderFactory = { storageManager, ktFiles ->
                FileBasedDeclarationProviderFactory(storageManager, ktFiles)
            }
        )

        container.getService(LazyTopDownAnalyzer::class.java).analyzeDeclarations(
            topDownAnalysisMode = TopDownAnalysisMode.TopLevelDeclarations,
            declarations = files
        )

        val moduleDescriptor = container.getService(ModuleDescriptor::class.java)

        return AnalysisResult.success(trace.bindingContext, moduleDescriptor).bindingContext
    }

    private fun extractUnresolvedReferences(
        file: KtFile,
        context: BindingContext
    ): List<KtNameReferenceExpression> {
        return file.collectDescendantsOfType<KtNameReferenceExpression> {
            context[BindingContext.REFERENCE_TARGET, it] == null
        }
    }
}
