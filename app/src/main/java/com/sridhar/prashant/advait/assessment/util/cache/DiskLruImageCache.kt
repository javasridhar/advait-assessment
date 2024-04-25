package com.sridhar.prashant.advait.assessment.util.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.jakewharton.disklrucache.DiskLruCache
import com.sridhar.prashant.advait.assessment.BuildConfig
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.Utils
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class DiskLruImageCache(
    context: Context, uniqueName: String, diskCacheSize: Long
) {
    private var mDiskCache: DiskLruCache? = null
//    private var mCompressFormat = CompressFormat.PNG
//    private var mCompressQuality = 100

    init {
        try {
            val diskCacheDir = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize)
//            mCompressFormat = compressFormat
//            mCompressQuality = quality
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class, FileNotFoundException::class)
    private fun writeBitmapToFile(bitmap: Bitmap, editor: DiskLruCache.Editor): Boolean {
        var out: OutputStream? = null
        return try {
            out = BufferedOutputStream(editor.newOutputStream(0), Constants.BUFFER_CACHE_SIZE)
            bitmap.compress(CompressFormat.JPEG, 50, out)
        } finally {
            out?.close()
        }
    }

    private fun getDiskCacheDir(context: Context, uniqueName: String): File {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        val cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
            !Utils.isExternalStorageRemovable()
        ) Utils.getExternalCacheDir(context)?.path else context.cacheDir.path
        return File(cachePath + File.separator + uniqueName)
    }

    fun put(key: String, data: Bitmap) {
        var editor: DiskLruCache.Editor? = null
        try {
            editor = mDiskCache?.edit(key)
            if (editor == null) {
                Log.d("Editor", "Null")
                return
            }
            if (writeBitmapToFile(data, editor)) {
                editor.commit()
                mDiskCache?.flush()
                if (BuildConfig.DEBUG) {
                    Log.d("cache_test_DISK_", "image put on disk cache $key")
                }
            } else {
                editor.abort()
                if (BuildConfig.DEBUG) {
                    Log.d("cache_test_DISK_", "ERROR on: image put on disk cache $key")
                }
            }
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                Log.d("cache_test_DISK_", "ERROR on: image put on disk cache $key")
            }
            try {
                editor?.abort()
            } catch (ignored: IOException) {
                ignored.printStackTrace()
            }
        }
    }

    fun getBitmap(key: String): Bitmap? {
        var bitmap: Bitmap? = null
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache?.get(key)
            if (snapshot == null) {
                return null
            }
            val `in`: InputStream = snapshot.getInputStream(0)
            if (`in` != null) {
                val buffIn = BufferedInputStream(`in`, Constants.BUFFER_CACHE_SIZE)
                bitmap = BitmapFactory.decodeStream(buffIn)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        if (BuildConfig.DEBUG) {
            Log.d("cache_test_DISK_", if (bitmap == null) "" else "image read from disk $key")
        }
        return bitmap
    }

    fun containsKey(key: String?): Boolean {
        var contained = false
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache?.get(key)
            contained = snapshot != null
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        return contained
    }

    fun clearCache() {
        if (BuildConfig.DEBUG) {
            Log.d("cache_test_DISK_", "disk cache CLEARED")
        }
        try {
            mDiskCache?.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val cacheFolder: File
        get() = mDiskCache!!.directory

    companion object {
        private const val APP_VERSION = 1
        private const val VALUE_COUNT = 1
        private const val TAG = "DiskLruImageCache"
    }

    private inner class DiskCacheWorker : DiskCacheTask<Bitmap?>() {
        override fun writeInDiskCache(t: Bitmap?): Boolean {
//            writeBitmapToFile()
            return false
        }

        override fun call(): Bitmap? {
            return null
        }

    }
}