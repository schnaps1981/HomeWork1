package com.example.homework1.utils

import android.content.Context
import androidx.annotation.StringRes

class ContextResReader(private val context: Context) : ResourceReader {

    override fun getString(@StringRes resId: Int, vararg values: Any) =
        context.getString(resId, *values)

}