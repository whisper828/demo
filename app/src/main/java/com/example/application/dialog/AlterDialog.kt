package com.example.application.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.application.R
import com.example.application.databinding.DialogAlertBinding
import com.example.application.ext.scanForActivity
import java.util.*
import kotlin.concurrent.schedule

/**
 * @date 2023/9/27
 * @Created by YeYun
 * @description
 */
class AlterDialog(context: Context) : Dialog(context) {

    private lateinit var mBinding: DialogAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window!!
        window.setGravity(Gravity.CENTER)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_alert,
            null,
            false
        )
        setContentView(mBinding.root)
        val windowManager = scanForActivity(context)?.windowManager
        val display = windowManager?.defaultDisplay
        val lp = window.attributes
        lp.width = display?.width!! * 4 / 5
        lp.y = -80
        window.attributes = lp
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        init()
    }

    private fun init(){
        Timer().schedule(2000, 3000) {
            dismiss()
            cancel()
        }
    }
}