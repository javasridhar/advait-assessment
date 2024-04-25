package com.sridhar.prashant.advait.assessment.util

import android.content.Context
import android.os.Environment


object Utils {

    @Synchronized
    fun getFormattedCacheKey(url: String): String {
        return url.filter { it.isLetterOrDigit() }.substringAfter("imagesimg")
    }

    fun isExternalStorageRemovable() = Environment.isExternalStorageRemovable()

    fun getExternalCacheDir(context: Context) = context.externalCacheDir
}