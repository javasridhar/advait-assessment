package com.sridhar.prashant.advait.assessment.util.cache

import java.util.concurrent.Callable

abstract class DiskCacheTask<T> : Callable<T> {
    abstract fun writeInDiskCache(t: T) : Boolean
}