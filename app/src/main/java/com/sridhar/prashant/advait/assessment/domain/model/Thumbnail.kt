package com.sridhar.prashant.advait.assessment.domain.model

import com.sridhar.prashant.advait.assessment.util.Constants

data class Thumbnail(
    val id: String,
    val domain: String,
    val basePath: String,
    val key: String
) {
    fun getImageUrl() = StringBuffer().apply {
        append(domain)
        append(Constants.SLASH_SEPARATOR)
        append(basePath)
        append(Constants.SLASH_SEPARATOR)
        append(Constants.ZERO_SEPARATOR)
        append(Constants.SLASH_SEPARATOR)
        append(key)
    }.toString()

    override fun toString() = getImageUrl()
}