package com.sridhar.prashant.advait.assessment.util.cache

import android.graphics.Bitmap
import androidx.collection.LruCache
import javax.inject.Singleton

private lateinit var cache: LruCache<String, Bitmap>

@Singleton
class MemoryCache private constructor() {

    init {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        cache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024 / 1024
            }
        }
    }

    @Synchronized
    fun addIntoCache(key: String, bitmap: Bitmap): Bitmap? {
        return cache.put(key, bitmap)
    }

    @Synchronized
    fun getFromCache(key: String) = cache[key]

    companion object {
        private var INSTANCE: MemoryCache? = null

        @Synchronized
        fun with(): MemoryCache {
            return INSTANCE ?: MemoryCache().also {
                INSTANCE = it
            }
        }
    }
}