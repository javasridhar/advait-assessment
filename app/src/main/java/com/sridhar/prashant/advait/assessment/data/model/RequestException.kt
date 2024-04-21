package com.sridhar.prashant.advait.assessment.data.model

data class RequestException(val code: Int, override val message: String) : Throwable(message)