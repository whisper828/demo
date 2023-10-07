package com.example.application.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.drake.channel.receiveTag
import com.example.application.constant.RxEvent

/**
 * Created by YeYun on 2021/11/3
 *
 * 基于ViewPager + tabLayout 实现的懒加载
 */
abstract class LazyFragment<T : ViewDataBinding> : Fragment() {


    lateinit var mBinding : T

    /*
     * 是否初始化布局
     */
    private var isViewInitiated = false

    /*
     * 当前界面是否可见
     */
    private var isVisibleToUser = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), setLayoutRes(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.root.fitsSystemWindows = true
        try {
            isViewInitiated = true
            isCanLoadData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        receiveTag(RxEvent.TokenEvent){
            onEvent()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        // 是否对用户可见
        this.isVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            isCanLoadData()
        }
    }

    /*
     * 执行数据加载： 条件是view初始化完成并且对用户可见
     */
    private fun isCanLoadData() {
        if (isViewInitiated && isVisibleToUser) {
            lazyLoad()
            // 加载过数据后，将isViewInitiated和isVisibleToUser设置成false，防止重复加载数据
            isViewInitiated = false
            isVisibleToUser = false
        }
    }

    protected abstract fun lazyLoad()

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract fun setLayoutRes(): Int


    fun toast(show : String) {
        Toast.makeText(context, show, Toast.LENGTH_SHORT).show()
    }

    protected abstract fun onEvent()

    open fun isPad(context: Context): Boolean {
        return ((context.getResources().getConfiguration().screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }
}