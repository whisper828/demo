package com.example.application.fragment

import android.graphics.Paint
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.TimeUtils
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.example.application.R
import com.example.application.base.BaseFragment
import com.example.application.data.TaskDecomposeModel
import com.example.application.databinding.FragmentDateBinding
import com.example.application.ext.deleteTask
import com.example.application.ext.getDataSources
import com.example.application.ext.gone
import com.example.application.ext.visible
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView

/**
 * @date 2023/9/25
 * @Created by YeYun
 * @description
 */
class DateFragment: BaseFragment<FragmentDateBinding>(), CalendarView.OnCalendarSelectListener {

    private val dataSource = mutableListOf<TaskDecomposeModel>()

    var isFirstIn = true

    override fun setLayoutRes(): Int {
        return R.layout.fragment_date
    }

    override fun initialize() {
        mBinding.topTitle = resources.getString(R.string.task_daily)
        mBinding.calendarView.setOnCalendarSelectListener(this)
        mBinding.recycler.linear().divider(R.drawable.divider_line_23).setup {
            addType<TaskDecomposeModel>(R.layout.item_decompose_display)

            onBind {
                val root = findView<LinearLayout>(R.id.action_bar_root)
                val cb = findView<CheckBox>(R.id.cb)
                val tvTitle = findView<TextView>(R.id.tv_title)
                val iconDelete = findView<ImageView>(R.id.icon_delete)
                val model = getModel<TaskDecomposeModel>()
                cb.isClickable = false
                root.setOnClickListener {
                    cb.isChecked = !cb.isChecked
                    if (cb.isChecked){
                        iconDelete.visible()
                        tvTitle.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        iconDelete.gone()
                        tvTitle.paint.flags = Paint.ANTI_ALIAS_FLAG
                    }
                }

                iconDelete.setOnClickListener {
                    deleteTask(model)
                    dataSource.removeAt(modelPosition)
                    notifyItemRemoved(modelPosition)
                }
            }
        }.models = dataSource

        mBinding.calendarView.scrollToCurrent()
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onCalendarSelect(calendar: Calendar, isClick: Boolean) {
        if (isClick || isFirstIn){
            isFirstIn = false
            var date = TimeUtils.millis2String(calendar.timeInMillis).split(" ")[0]
            dataSource.clear()
            val doubleArray = getDataSources()
            for (element in doubleArray){
                if (element.taskDate == date){
                    dataSource.add(element)
                }
            }
            mBinding.recycler.bindingAdapter.models = dataSource
        }
    }

}