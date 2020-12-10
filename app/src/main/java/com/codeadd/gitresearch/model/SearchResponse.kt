package com.codeadd.gitresearch.model

data class SearchResponse(
        val total_count: Int,
        val items: MutableList<Repo>
        )