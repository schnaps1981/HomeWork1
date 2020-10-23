package com.example.homework1.utils

import androidx.annotation.StringRes

interface ResourceReader {
    fun getString(@StringRes resId: Int, vararg values: Any): String
}