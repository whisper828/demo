package com.example.application.fragment

import androidx.viewpager2.widget.ViewPager2
import com.drake.channel.receiveTag
import com.example.application.R
import com.example.application.base.BaseFragment
import com.example.application.constant.RxEvent
import com.example.application.databinding.FragmentMainBinding
import com.example.application.ext.init
import com.example.application.ext.initMain
import com.example.application.ext.interceptLongClick

/**
 * Created by YeYun on 2021/10/14
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun setLayoutRes(): Int {
        return R.layout.fragment_main
    }

    override fun initialize() {
        //初始化viewpager2
        mBinding.mainViewpager.initMain(this)
        mBinding.mainViewpager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                mBinding.mainBottom.currentItem = position
            }
        })
        //初始化 bottomBar
        mBinding.mainBottom.init{
            when (it) {
                R.id.menu_main -> mBinding.mainViewpager.setCurrentItem(0, false)
                R.id.menu_task -> mBinding.mainViewpager.setCurrentItem(1, false)
                R.id.menu_date -> mBinding.mainViewpager.setCurrentItem(2, false)
            }
        }
        mBinding.mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_task, R.id.menu_date)


        receiveTag(RxEvent.TaskCreated){
            mBinding.mainViewpager.currentItem = 0
        }
    }

}