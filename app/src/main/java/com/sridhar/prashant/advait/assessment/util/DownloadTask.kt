package com.sridhar.prashant.advait.assessment.util

import java.util.concurrent.Callable

abstract class DownloadTask<T> : Callable<T> {
    abstract fun download(url: String) : T
}