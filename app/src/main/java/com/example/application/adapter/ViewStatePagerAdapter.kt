package com.example.application.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter




/**
 * author: Jasmine
 * 创建时间: 2022/5/9
 * Describe:
 */
class ViewStatePagerAdapter(fm : FragmentManager, private val list : List<Fragment>, private val titles:List<String>) :FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return  list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}