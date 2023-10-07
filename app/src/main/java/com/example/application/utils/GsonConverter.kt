package com.example.application.utils

import com.drake.net.convert.JSONConvert
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class GsonConverter : JSONConvert() {
    private val gson = GsonBuilder().serializeNulls().create()

    override fun <R> String.parseBody(succeed: Type): R? {
        return gson.fromJson(this, succeed)
    }
}