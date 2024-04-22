package com.sridhar.prashant.advait.assessment.util.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.collection.LruCache
import com.sridhar.prashant.advait.assessment.util.Utils
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Singleton

private lateinit var memoryCache: LruCache<String, Bitmap>

@Singleton
class ImageDownloader private constructor() {
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    init {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, value: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                Log.d("ItemSize", "${value.byteCount / 1024 / 1024}")
                return value.byteCount / 1024 / 1024
            }
        }
    }

    fun load(url: String) : Future<Bitmap?> {
        val futureBitmap: Future<Bitmap?>
        val downloadTask = Downloader(url)
        futureBitmap = executorService.submit(downloadTask)
        return futureBitmap
    }

    companion object {

        private var INSTANCE: ImageDownloader? = null

        @Synchronized
        fun with(): ImageDownloader {
            return INSTANCE ?: ImageDownloader().also {
                INSTANCE = it
            }
        }
    }

    private inner class Downloader(private val url: String) : DownloadTask<Bitmap?>() {

        override fun download(url: String): Bitmap? {

            var bitmap: Bitmap? = null
            val formattedCacheKey = Utils.getFormattedCacheKey(url)

            if (memoryCache[formattedCacheKey] == null) {
                Log.d("ImageDownloaderNotOk", "CacheNotOk $formattedCacheKey")
                try {
                    val urlObj = URL(url)
                    val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
                    bitmap = BitmapFactory.decodeStream(conn.inputStream).also {
                        Log.d("ImageDownloaderPut", "${Utils.getFormattedCacheKey(conn.url.toString())} ${memoryCache.put(formattedCacheKey, it)}")
                        Log.d("ImageDownloaderGet", "${Utils.getFormattedCacheKey(conn.url.toString())} ${memoryCache[formattedCacheKey]}")
                    }
                    conn.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Log.d("ImageDownloaderOk", "CacheOk $formattedCacheKey")
                bitmap = memoryCache[formattedCacheKey]
            }
            return bitmap
        }

        override fun call(): Bitmap? {
            return download(url)
        }
    }
}