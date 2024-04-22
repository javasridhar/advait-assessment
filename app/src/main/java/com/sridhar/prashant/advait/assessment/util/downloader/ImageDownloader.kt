package com.sridhar.prashant.advait.assessment.util.downloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Singleton

@Singleton
class ImageDownloader private constructor() {
    private val map: LinkedHashMap<String, Bitmap> = linkedMapOf()
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

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
//            val cache = MemoryCache.with()
//            val formattedCacheKey = Utils.getFormattedCacheKey(url)
            val formattedCacheKey = url

//            if (cache.getFromCache(formattedCacheKey) == null) {
            if (map[formattedCacheKey] == null) {
//                Log.d("ImageDownloader", "NoCache $formattedCacheKey")
                try {
                    val urlObj = URL(url)
                    val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
                    bitmap = BitmapFactory.decodeStream(conn.inputStream)
                    conn.disconnect()
                    if (map.size > 100) {
                        map.values.remove(map.values.last())
                    }
                    map[formattedCacheKey] = bitmap
//                    MemoryCache.with().addIntoCache(formattedCacheKey, bitmap!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
//                Log.d("ImageDownloader", "CacheOk $formattedCacheKey")
//                bitmap = cache.getFromCache(formattedCacheKey)
                bitmap = map[formattedCacheKey]
            }
            return bitmap
        }

        override fun call(): Bitmap? {
            return download(url)
        }
    }
}