package com.sridhar.prashant.advait.assessment.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ImageDownloader private constructor() {
    private val executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    fun loadDrawable(url: String) : Future<Drawable?> {
        val drawable: Future<Drawable?>
        val downloadTask = DownloaderDrawable(url)
        drawable = executorService.submit(downloadTask)
        return drawable
    }

    fun load(url: String) : Future<Bitmap?> {
        val bm: Future<Bitmap?>
        val downloadTask = Downloader(url)
        bm = executorService.submit(downloadTask)
        return bm
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
                e.printStackTrace()
            }
            return bitmap
        }

        override fun call(): Bitmap? {
            return download(url)
        }
    }
}