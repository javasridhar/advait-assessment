package com.sridhar.prashant.advait.assessment.util.cache

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.util.Log
import com.jakewharton.disklrucache.DiskLruCache
import com.sridhar.prashant.advait.assessment.util.Constants
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private lateinit var cache: DiskLruCache
@Suppress("DEPRECATION")
class DiskCache(context: Context) {

    private val cacheLock = ReentrantLock()
    private val diskCacheLockCondition: Condition = cacheLock.newCondition()
    private var diskCacheStarting = true

    init {
        val cacheDir = getDiskCacheDir(context, Constants.DISK_CACHE_SUBDIR)
        InitDiskCacheTask().execute(cacheDir)
    }

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
    // but if not mounted, falls back on internal storage.
    private fun getDiskCacheDir(context: Context, uniqueName: String): File {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        val cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !isExternalStorageRemovable()) {
                context.externalCacheDir?.path
            } else {
                context.cacheDir.path
            }

        Log.d("DiskCachePath", "$cachePath")

        return File(cachePath + File.separator + uniqueName)
    }

    internal inner class InitDiskCacheTask : AsyncTask<File, Void, Void>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: File): Void? {
            cacheLock.withLock {
                val cacheDir = params[0]
                cache = DiskLruCache.open(cacheDir, 1, 1, Constants.DISK_CACHE_SIZE)
                diskCacheStarting = false // Finished initialization
                diskCacheLockCondition.signalAll() // Wake any waiting threads
            }
            return null
        }
    }

    internal inner class  BitmapWorkerTask : AsyncTask<Int, Unit, Bitmap>() {
        // Decode image in background.
        override fun doInBackground(vararg params: Int?): Bitmap? {
            val imageKey = params[0].toString()

            // Check disk cache in background thread
            return getBitmapFromDiskCache(imageKey)
        }
    }

    @Throws(IOException::class)
    fun addBitmapToCache(key: String, bitmap: Bitmap) {
        synchronized(cacheLock) {
            val os = cache.edit(key).newOutputStream(0)
            val bos = BufferedOutputStream(os, Constants.BUFFER_CACHE_SIZE)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        }
    }

    fun getBitmapFromDiskCache(key: String): Bitmap? =
        cacheLock.withLock {
            // Wait while disk cache is started from background thread
            while (diskCacheStarting) {
                try {
                    diskCacheLockCondition.await()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            return null
        }
}