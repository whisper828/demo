package com.example.application.ext

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper

/**
 * Created by YeYun on 2021/11/5
 */
fun Dialog.scanForActivity(cont: Context?): Activity? {
    return when (cont) {
        null -> null
        is Activity -> cont
        is ContextWrapper -> scanForActivity(
            cont.baseContext
        )
        else -> null
    }
}