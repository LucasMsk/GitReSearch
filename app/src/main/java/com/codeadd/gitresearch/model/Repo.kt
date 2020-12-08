package com.codeadd.gitresearch.model

data class Repo (
    val name: String,
    val full_name: String,
    val html_url: String,
    val owner: Owner,
    val stargazers_count: Int
)