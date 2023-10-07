package com.example.application.ext

import android.app.Application
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.example.application.fragment.DateFragment
import com.example.application.fragment.HomeFragment
import com.example.application.fragment.TaskFragment
import com.example.application.utils.SettingUtil

/**
 * Created by YeYun on 2021/10/14
 */
lateinit var app: Application

fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 3
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    HomeFragment()
                }
                1 -> {
                    TaskFragment()
                }
                else -> {
                    DateFragment()
                }
            }
        }
        override fun getItemCount() = 3
    }
    return this
}

fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(app), app)
    itemTextColor = SettingUtil.getColorStateList(app)
    setTextSize(12F)
    itemHeight += 10
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}

/**
 * 拦截BottomNavigation长按事件 防止长按时出现Toast ---- 追求完美的大屌群友提的bug
 * @receiver BottomNavigationViewEx
 * @param ids IntArray
 */
fun BottomNavigationViewEx.interceptLongClick(vararg ids:Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices){
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index]).setOnLongClickListener {
            true
        }
    }
}

private var lastClickTime: Long = 0

//过滤短时间内多次调用
fun noFastClick(): Boolean {
    val time = System.currentTimeMillis()
    if (time - lastClickTime > 500) {
        return true
    }
    lastClickTime = time
    return false
}

