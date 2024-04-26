package com.sridhar.prashant.advait.assessment.util.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.os.Environment
import com.jakewharton.disklrucache.DiskLruCache
import com.sridhar.prashant.advait.assessment.util.Constants
import com.sridhar.prashant.advait.assessment.util.Utils
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

private lateinit var mDiskCache: DiskLruCache

@Singleton
class DiskLruImageCache(
    context: Context, uniqueName: String, diskCacheSize: Long
) {
    init {
        try {
            val diskCacheDir = getDiskCacheDir(context, uniqueName)
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, diskCacheSize)
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

    @Synchronized
    fun put(key: String, data: Bitmap) {
        var editor: DiskLruCache.Editor? = null
        try {
            editor = mDiskCache?.edit(key)
            if (editor == null) {
                return
            }
            if (writeBitmapToFile(data, editor)) {
                editor.commit()
                mDiskCache?.flush()
            } else {
                editor.abort()
            }
        } catch (e: IOException) {
            try {
                editor?.abort()
            } catch (ignored: IOException) {
                ignored.printStackTrace()
            }
        }
    }

    @Synchronized
    fun getBitmap(key: String): Bitmap? {
        var bitmap: Bitmap? = null
        var snapshot: DiskLruCache.Snapshot? = null
        try {
            snapshot = mDiskCache?.get(key)
            if (snapshot == null) {
                return null
            }
            val `in`: InputStream = snapshot.getInputStream(0)
            val buffIn = BufferedInputStream(`in`, Constants.BUFFER_CACHE_SIZE)
            bitmap = BitmapFactory.decodeStream(buffIn)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            snapshot?.close()
        }
        return bitmap
    }

    companion object {
        private const val APP_VERSION = 1
        private const val VALUE_COUNT = 1
    }
}