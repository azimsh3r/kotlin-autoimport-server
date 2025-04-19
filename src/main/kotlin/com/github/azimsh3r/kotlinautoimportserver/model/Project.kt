package com.github.azimsh3r.kotlinautoimportserver.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown=true)
data class Project(
    val args: String = "",
    val files: List<ProjectFile> = listOf(),
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProjectFile(val text: String = "", val name: String = "")
