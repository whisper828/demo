package com.example.application.ext

import android.view.View

/**
 * Created by YeYun on 2021/10/15
 */

fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}