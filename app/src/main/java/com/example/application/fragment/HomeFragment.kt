package com.example.application.fragment

import android.graphics.Paint
import android.widget.*
import com.bumptech.glide.Glide
import com.drake.brv.utils.*
import com.drake.channel.receiveEvent
import com.drake.channel.receiveTag
import com.drake.channel.sendEvent
import com.drake.net.utils.runMain
import com.example.application.R
import com.example.application.base.BaseFragment
import com.example.application.constant.RxEvent
import com.example.application.data.*
import com.example.application.databinding.FragmentHomeBinding
import com.example.application.ext.*
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by YeYun on 2021/10/14
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var dataSourceTop = mutableListOf<TaskDecomposeModel>()
    private var dataSourceBottom = mutableListOf<TaskDecomposeModel>()

    private var topExpand = true
    private var bottomExpand = true

    override fun setLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initialize() {
        mBinding.topTitle = resources.getString(R.string.task_now)

        mBinding.banner.setBannerData(mutableListOf(BannerModel(localImageUrl = R.mipmap.ic_banner_bg)))
        mBinding.banner.loadImage { _, model, view, _ ->
            Glide.with(requireContext()).load((model as BannerModel).localImageUrl)
                .into(view as ImageView)
        }

        mBinding.recyclerTop.linear().divider(R.drawable.divider_line_23).setup {
            addType<TaskDecomposeModel>(R.layout.item_decompose_model)
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
                    cb.isChecked = false
                    iconDelete.gone()
                    tvTitle.paint.flags = Paint.ANTI_ALIAS_FLAG
                    deleteTask(model)
                    sendEvent(BelowEvent(taskId = model.id))
                    dataSourceTop.removeAt(modelPosition)
                    notifyItemRemoved(modelPosition)
                }
            }
        }

        mBinding.recyclerBelow.linear().divider(R.drawable.divider_line_23).setup {
            addType<TaskDecomposeModel>(R.layout.item_decompose_model)
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
                    cb.isChecked = false
                    iconDelete.gone()
                    tvTitle.paint.flags = Paint.ANTI_ALIAS_FLAG
                    deleteTask(model)
                    sendEvent(TopEvent(taskId = model.id))
                    dataSourceBottom.removeAt(modelPosition)
                    notifyItemRemoved(modelPosition)
                }
            }
        }

        mBinding.rootTop.setOnClickListener {
            topExpand = !topExpand
            if (topExpand){
                mBinding.recyclerTop.visible()
                mBinding.iconIndicatorTop.setImageResource(R.drawable.icon_arrow_down)
            }else{
                mBinding.recyclerTop.gone()
                mBinding.iconIndicatorTop.setImageResource(R.drawable.icon_arrow_up)
            }
        }

        mBinding.rootBelow.setOnClickListener {
            bottomExpand = !bottomExpand
            if (bottomExpand){
                mBinding.recyclerBelow.visible()
                mBinding.iconIndicatorBelow.setImageResource(R.drawable.icon_arrow_down)
            }else{
                mBinding.recyclerBelow.gone()
                mBinding.iconIndicatorBelow.setImageResource(R.drawable.icon_arrow_up)
            }
        }

        receiveEvent<TopEvent> {
            var position = -1
            for ((index, element) in dataSourceTop.withIndex()){
                if (element.id == it.taskId){
                    position = index
                }
            }
            if (position >= 0){
                dataSourceTop.removeAt(position)
                mBinding.recyclerTop.bindingAdapter.notifyItemRemoved(position)
            }
        }

        receiveEvent<BelowEvent> {
            var position = -1
            for ((index, element) in dataSourceBottom.withIndex()){
                if (element.id == it.taskId){
                    position = index
                }
            }
            if (position >= 0){
                dataSourceBottom.removeAt(position)
                mBinding.recyclerBelow.bindingAdapter.notifyItemRemoved(position)
            }
        }

        mBinding.page.onRefresh {
            Timer().schedule(800, 3000) {
                runMain {
                    showContent()
                    dataSourceTop.clear()
                    dataSourceBottom.clear()
                    dataSourceTop.addAll(getAllTask())
                    mBinding.recyclerTop.models = dataSourceTop
                    dataSourceBottom.addAll(getTodayTask())
                    mBinding.recyclerBelow.models = dataSourceBottom
                }
                cancel()
            }
        }.showLoading()

        receiveTag(RxEvent.TaskCreated){
            mBinding.page.showLoading()
        }
    }
}