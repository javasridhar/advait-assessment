package com.sridhar.prashant.advait.assessment.domain.model

data class Thumbnail(
    val id: String,
    val domain: String,
    val basePath: String,
    val key: String
) {
    fun getImageUrl() = StringBuffer().apply {
        append(domain)
        append("/")
        append(basePath)
        append("/0/")
        append(key)
    }.toString()

    override fun toString() = getImageUrl()
}