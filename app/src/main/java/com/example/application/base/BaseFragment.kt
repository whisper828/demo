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

/***
 * @author YeYun Chen
 * @date 2020/10/30  15:06 
 * @E-mail : 617485685@qq.com
 *
 * ViewPager2  懒加载实现
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var mBinding : T

    private var isFirstIn = true // 是否第一次加载

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), setLayoutRes(), container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.root.fitsSystemWindows = true
    }

    override fun onResume() {
        super.onResume()
        if (isFirstIn) {
            try {
                initialize()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isFirstIn = false
        }

    }

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    @LayoutRes
    protected abstract fun setLayoutRes(): Int

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     */
    @Throws(java.lang.Exception::class)
    protected abstract fun initialize()


    fun toast(show : String) {
        Toast.makeText(context, show, Toast.LENGTH_SHORT).show()
    }

    open fun isPad(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

}