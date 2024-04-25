package com.sridhar.prashant.advait.assessment.util.downloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.Utils
import com.sridhar.prashant.advait.assessment.util.cache.DiskLruImageCache
import com.sridhar.prashant.advait.assessment.util.cache.MemoryCache
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import javax.inject.Singleton

private lateinit var memoryCache: MemoryCache
//private lateinit var diskCache: DiskCache
private lateinit var diskCache: DiskLruImageCache

@Singleton
class ImageDownloader private constructor() {
    private var isDiskCacheEnabled = false
    private var isMemoryCacheEnabled = false
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

//    fun load(url: String) : Future<Bitmap?> {
//        val futureBitmap: Future<Bitmap?>
//        val downloadTask = Downloader(url)
//        futureBitmap = executorService.submit(downloadTask)
//        return futureBitmap
//    }

    fun loadBitmap(url: String) : Bitmap? {
        val key = Utils.getFormattedCacheKey(url)
        val downloadTask = Downloader(url)

        val memoryCacheValue: Bitmap? = memoryCache?.getFromCache(key)
        val diskCacheValue: Bitmap? = diskCache?.getBitmap(key)

        return memoryCacheValue
            ?: if (diskCacheValue != null) {
                // store in memoryCache too
                Log.d("ImageDisk", "not null disk")
                memoryCache?.addIntoCache(key, diskCacheValue)
                diskCacheValue
            } else {
                Log.d("ImageDisk", "null disk")
                val bitmap = executorService.submit(downloadTask).get()
                // store in diskCache and memoryCache
                memoryCache?.addIntoCache(key, bitmap!!)
                diskCache?.put(key, bitmap!!)

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

            var bitmap: Bitmap? = null

            try {
                val urlObj = URL(url)
                val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
                bitmap = BitmapFactory.decodeStream(conn.inputStream)
                conn.disconnect()
            } catch (e: Exception) {
                Log.d("URLException", url)
                e.printStackTrace()
            }
            return bitmap
        }

        override fun call(): Bitmap? {
            return download(url)
        }
    }
}