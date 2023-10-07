package com.example.application.utils

import android.content.Context

fun Context.versionCode(): Int {
    return packageManager.getPackageInfo(packageName, 0).versionCode
}

fun Context.versionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}