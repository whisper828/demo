package com.example.application.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.TimeUtils
import com.example.application.R
import com.example.application.databinding.DialogDateSelectorBinding
import com.example.application.ext.gone
import com.example.application.ext.scanForActivity
import com.example.application.utils.AnimUtils
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView

/**
 * @Date 2022/10/19
 * @Created by YeYun
 * @Description 日期选择器
 */
class DateSelector(
    context: Context,
    val listener: DateSelectedListener,
) : Dialog(context), CalendarView.OnCalendarSelectListener,
    CalendarView.OnYearChangeListener, CalendarView.OnMonthChangeListener {

    lateinit var mBinding: DialogDateSelectorBinding

    private var mYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_date_selector,
            null,
            false
        )
        setContentView(mBinding.root)
        val window = window!!
        val windowManager = scanForActivity(context)?.windowManager
        val display = windowManager?.defaultDisplay
        val lp = window.attributes
        lp.width = display?.width!! * 9 / 10
        lp.y = -80
        window.attributes = lp
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        initUI()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initUI() {
        mBinding.tvMonthDay.setOnClickListener(View.OnClickListener {
            if (!mBinding.calendarLayout.isExpand) {
                mBinding.calendarLayout.expand()
                return@OnClickListener
            }
            mBinding.calendarView.showYearSelectLayout(mYear)
            mBinding.tvLunar.gone()
            mBinding.tvYear.gone()
            mBinding.tvMonthDay.text = mYear.toString()
        })
        mBinding.flCurrent.setOnClickListener {
            AnimUtils.setOnClickAnimEndListener(mBinding.flCurrent, context) {
                mBinding.calendarView.scrollToCurrent()
            }
        }

        mBinding.calendarView.setOnCalendarSelectListener(this)
        mBinding.calendarView.setOnYearChangeListener(this)
        mBinding.calendarView.setOnMonthChangeListener(this)
        mBinding.tvYear.text = mBinding.calendarView.curYear.toString()
        mYear = mBinding.calendarView.curYear
        mBinding.tvMonthDay.text = mBinding.calendarView.curMonth.toString() + "月" + mBinding.calendarView.curDay + "日"
        mBinding.tvLunar.text = "今日"
        mBinding.tvCurrentDay.text = mBinding.calendarView.curDay.toString()

        mBinding.calendarView.post {
            mBinding.calendarView.scrollToCurrent()
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    @SuppressLint("SetTextI18n")
    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        if (isClick) {
            listener.onConfirm(TimeUtils.millis2String(calendar?.timeInMillis!!).split(" ")[0])
            dismiss()
        }
    }

    override fun onYearChange(year: Int) {
        mBinding.tvMonthDay.text = year.toString()
    }

    override fun onMonthChange(year: Int, month: Int) {

    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String,
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

    interface DateSelectedListener {
        fun onConfirm(date: String)
    }
}