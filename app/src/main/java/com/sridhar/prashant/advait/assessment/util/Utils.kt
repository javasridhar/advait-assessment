package com.sridhar.prashant.advait.assessment.util

object Utils {

    @Synchronized
    fun getFormattedCacheKey(url: String): String {
        return url.filter { it.isLetterOrDigit() }.substringAfter("imagesimg")
    }
}