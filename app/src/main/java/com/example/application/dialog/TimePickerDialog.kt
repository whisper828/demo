package com.example.application.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.example.application.R
import com.example.application.databinding.DialogTimePickerBinding
import java.util.*


/**
 * @Date 2021/12/27
 * @Created by YeYun
 * @Description 时间选择器
 */
class TimePickerDialog(
    context: Context,
    style: Int,
    val listener: TimeListener
) : Dialog(context, style) {

    lateinit var mBinding: DialogTimePickerBinding

    private var mHourOfDay: Int = Date().hours
    private var mMinute: Int = Date().minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_time_picker,
            null,
            false
        )
        setContentView(mBinding.root)
        val window = window!!
        val lp = window.attributes
        lp.y = -80
        window.attributes = lp
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        init()
    }

    private fun init() {

        mBinding.timePicker.descendantFocusability = TimePicker.FOCUS_BLOCK_DESCENDANTS //设置点击事件不弹键盘

        mBinding.timePicker.setIs24HourView(true) //设置时间显示为24小时
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.timePicker.hour = Date().hours //设置当前小时
            mBinding.timePicker.minute = Date().minutes //设置当前分（0-59）
        }
        mBinding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            mHourOfDay = hourOfDay
            mMinute = minute
        }

        mBinding.confirm.setOnClickListener {
            listener.onSelected(mHourOfDay, mMinute)
            dismiss()
        }

        mBinding.cancel.setOnClickListener {
            dismiss()
        }
    }

    interface TimeListener {
        fun onSelected(timeHour: Int, timeMinute: Int)
    }
}