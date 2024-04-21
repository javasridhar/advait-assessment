package com.sridhar.prashant.advait.assessment.util

import android.graphics.drawable.Drawable
import java.net.HttpURLConnection
import java.net.URL

class DownloaderDrawable(private val url: String) : DownloadTask<Drawable?>() {

    override fun download(url: String): Drawable? {
        var drawable: Drawable? = null
        try {
            val urlObj = URL(url)
            val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
            drawable = Drawable.createFromStream(conn.inputStream, null)
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return drawable
    }

    override fun call(): Drawable? {
        return download(url)
    }
}