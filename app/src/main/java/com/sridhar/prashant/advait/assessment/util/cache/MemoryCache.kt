package com.sridhar.prashant.advait.assessment.util.cache

import android.graphics.Bitmap
import android.util.Log
import androidx.collection.LruCache
import javax.inject.Singleton

@Singleton
class MemoryCache private constructor() {

    private val maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
    private val cache: LruCache<String, Bitmap>

    init {
        cache = object : LruCache<String, Bitmap>(maxCacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    @Synchronized
    fun addIntoCache(key: String, bitmap: Bitmap): Bitmap? {
        val result = cache.put(key, bitmap)
        Log.d("MemoryCache", "$key ${cache.hitCount()} Result => ${result != null}")
        return result
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