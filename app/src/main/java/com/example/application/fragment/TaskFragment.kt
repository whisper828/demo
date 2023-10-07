package com.example.application.fragment

import android.annotation.SuppressLint
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.drake.channel.sendTag
import com.example.application.R
import com.example.application.base.BaseFragment
import com.example.application.constant.RxEvent
import com.example.application.data.BannerModel
import com.example.application.data.TaskDecomposeModel
import com.example.application.databinding.FragmentContactBinding
import com.example.application.dialog.AlterDialog
import com.example.application.dialog.DateSelector
import com.example.application.dialog.TimePickerDialog
import com.example.application.ext.addTask
import com.example.application.utils.AnimUtils

class TaskFragment : BaseFragment<FragmentContactBinding>() {

    private lateinit var taskDecomposeModel: TaskDecomposeModel

    override fun setLayoutRes(): Int {
        return R.layout.fragment_contact
    }

    override fun initialize() {
        mBinding.topTitle = resources.getString(R.string.task_add)

        mBinding.banner.setBannerData(mutableListOf(BannerModel(localImageUrl = R.mipmap.ic_banner_bg)))
        mBinding.banner.loadImage { _, model, view, _ ->
            Glide.with(requireContext()).load((model as BannerModel).localImageUrl)
                .into(view as ImageView)
        }

        taskDecomposeModel = TaskDecomposeModel(id = System.currentTimeMillis())

        mBinding.layoutTaskDate.setOnClickListener {
            DateSelector(requireContext(),
                object : DateSelector.DateSelectedListener {
                    override fun onConfirm(date: String) {
                        toast(date)
                        mBinding.taskDate.text = Editable.Factory().newEditable(date)
                        taskDecomposeModel.taskDate = date
                    }

                }).show()
        }

        mBinding.layoutTaskTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                R.style.bottomDialog,
                object : TimePickerDialog.TimeListener {
                    @SuppressLint("SetTextI18n")
                    override fun onSelected(timeHour: Int, timeMinute: Int) {
                        val mHourOfDay = if (timeHour < 10) {
                            "0${timeHour}"
                        } else {
                            timeHour.toString()
                        }
                        val mMinute = if (timeMinute < 10) {
                            "0${timeMinute}"
                        } else {
                            timeMinute.toString()
                        }
                        mBinding.taskTime.text = Editable.Factory().newEditable("$mHourOfDay:$mMinute")
                        taskDecomposeModel.taskTime = "$mHourOfDay:$mMinute"
                    }
                }).show()
        }

        mBinding.spinnerRing.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskDecomposeModel.taskRing = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        mBinding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskDecomposeModel.taskType = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        mBinding.taskAdd.setOnClickListener {
            AnimUtils.setOnClickAnimEndListener(mBinding.taskAdd, context){
                taskDecomposeModel.title = mBinding.editTaskTitle.text.toString()
                if (taskDecomposeModel.isEmpty()){
                    toast("请完整填写任务内容")
                    return@setOnClickAnimEndListener
                }
                addTask(decomposeModel = taskDecomposeModel)
                AlterDialog(requireContext()).apply {
                    setOnDismissListener {
                        sendTag(RxEvent.TaskCreated)
                        complete()
                    }
                    show()
                }
            }
        }
    }

    private fun complete(){
        taskDecomposeModel = TaskDecomposeModel(title = "", taskTime = "", taskDate = "", taskType = 0, taskRing = 0, checked = false)
        mBinding.taskDate.text = resources.getString(R.string.task_date)
        mBinding.taskTime.text = resources.getString(R.string.task_time)
        mBinding.spinnerRing.setSelection(0)
        mBinding.spinnerType.setSelection(0)
        mBinding.editTaskTitle.text = Editable.Factory().newEditable("")
    }

}