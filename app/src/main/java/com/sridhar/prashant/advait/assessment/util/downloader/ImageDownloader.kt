package com.sridhar.prashant.advait.assessment.util.downloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.Utils
import com.sridhar.prashant.advait.assessment.util.cache.DiskLruImageCache
import com.sridhar.prashant.advait.assessment.util.cache.MemoryCache
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Singleton

private lateinit var memoryCache: MemoryCache
private lateinit var diskCache: DiskLruImageCache

@Singleton
class ImageDownloader private constructor() {
    private var isDiskCacheEnabled = false
    private var isMemoryCacheEnabled = false
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun loadBitmap(url: String) : Bitmap? {
        val key = Utils.getFormattedCacheKey(url)
        val downloadTask = Downloader(url)

        val memoryCacheValue: Bitmap? = memoryCache?.getFromCache(key)
        val diskCacheValue: Bitmap? = diskCache?.getBitmap(key)

        return memoryCacheValue
            ?: if (diskCacheValue != null) {
                // store in memoryCache too
                memoryCache?.addIntoCache(key, diskCacheValue)
                diskCacheValue
            } else {
                val bitmap = executorService.submit(downloadTask).get()
                // store in diskCache and memoryCache if caching is true
                bitmap?.let {
                    memoryCache?.addIntoCache(key, bitmap)
                    diskCache?.put(key, bitmap)
                }
                bitmap
            }
    }

    fun memoryCache(isMemoryCacheEnabled: Boolean = false) : ImageDownloader {
        this.isMemoryCacheEnabled = isMemoryCacheEnabled
        memoryCache = MemoryCache.with()
        return this
    }

    fun diskCache(isDiskCacheEnabled: Boolean = false, context: Context) : ImageDownloader {
        this.isDiskCacheEnabled = isDiskCacheEnabled
        diskCache = DiskLruImageCache(context, Constants.DISK_CACHE_SUBDIR, Constants.DISK_CACHE_SIZE)
        return this
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

            val urlObj = URL(url)
            val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
            val bitmap: Bitmap? = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
            return bitmap
        }

        override fun call(): Bitmap? {
            return download(url)
        }
    }
}