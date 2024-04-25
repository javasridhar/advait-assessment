package com.sridhar.prashant.advait.assessment.util

object Constants {
    const val LIST_URL = "https://acharyaprashant.org/api/v2/content/misc/media-coverages?limit=100"
    const val GRID_COLUMN_COUNT = 3
    const val READ_TIME_OUT = 1L
    const val CALL_TIME_OUT = 1L
    const val WRITE_TIME_OUT = 1L
    const val DISK_CACHE_SUBDIR = "assessment-cache-dir"
    const val DISK_CACHE_SIZE: Long = 100 * 1024 * 1024 // 100 MB
    const val BUFFER_CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
}