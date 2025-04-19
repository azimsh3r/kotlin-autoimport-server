package com.github.azimsh3r.kotlinautoimportserver.model

data class ImportSuggestion(
    val name: String,
    val candidates: List<String>
)
