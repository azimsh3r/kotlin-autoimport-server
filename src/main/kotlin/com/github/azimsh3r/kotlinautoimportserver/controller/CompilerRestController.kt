package com.github.azimsh3r.kotlinautoimportserver.controller

import com.github.azimsh3r.kotlinautoimportserver.model.ImportSuggestion
import com.github.azimsh3r.kotlinautoimportserver.model.Project
import com.github.azimsh3r.kotlinautoimportserver.service.AutoImportProviderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/compiler"])
class CompilerRestController(private val autoImportProviderService: AutoImportProviderService) {

    @PostMapping("/autoimport")
    fun getAutoImportSuggestions(@RequestBody project: Project) : List<ImportSuggestion> {
        return autoImportProviderService.suggestImports(project)
    }
}
