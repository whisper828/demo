package com.example.application.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.res.Configuration
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.LogUtils
import com.drake.channel.receiveTag
import com.example.application.constant.RxEvent
import com.example.application.utils.StatusBar
import android.content.SharedPreferences
import android.content.res.Resources
import java.util.*
import kotlin.collections.ArrayList
import android.os.LocaleList

import android.os.Build

import android.util.DisplayMetrics
import androidx.core.os.LocaleListCompat

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) :
    AppCompatActivity(contentLayoutId), OnClickListener {

    lateinit var mBinding: B
    lateinit var rootView: View

    private var finishBroadcastReceiver: BroadcastReceiver? = null
    private val onBackPressInterceptors = ArrayList<() -> Boolean>()
    private var onTouchEvent: (MotionEvent.() -> Boolean)? = null

    override fun setContentView(layoutResId: Int) {
        rootView = layoutInflater.inflate(layoutResId, null)
        setApplicationLanguage(this)
        setContentView(rootView)
        mBinding = DataBindingUtil.bind(rootView)!!
        init()
    }

    open fun init() {
        StatusBar.lightStatusBar(this, light = false)

        try {
            initView()
            initData()
        } catch (e: Exception) {
            LogUtils.e("初始化失败")
            e.printStackTrace()
        }
    }

    /**
     * 设置语言类型
     */
    open fun setApplicationLanguage(context: Context) {
        val resources: Resources = context.applicationContext.resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        val locale = Locale("vi")
//        val locale = Locale.forLanguageTag("vi")
        LocaleListCompat.forLanguageTags("vi_rVN")
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            context.applicationContext.createConfigurationContext(config)
            Locale.setDefault(locale)
        }
        resources.updateConfiguration(config, dm)
    }

    protected abstract fun initView()
    protected abstract fun initData()

    override fun onClick(v: View) {}

    /**activity_splash
     * 触摸事件
     * @param block 返回值表示是否拦截事件
     */
    fun onTouchEvent(block: MotionEvent.() -> Boolean) {
        onTouchEvent = block
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val b = super.dispatchTouchEvent(event)
        return onTouchEvent?.invoke(event) ?: b
    }

    /**
     * 返回键事件
     * @param block 返回值表示是否拦截事件
     */
    fun onBackPressed(block: () -> Boolean) {
        onBackPressInterceptors.add(block)
    }

    override fun onBackPressed() {
        onBackPressInterceptors.forEach {
            if (it.invoke()) return
        }
        super.onBackPressed()
    }

    open fun isPad(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    /**
     * 关闭界面
     */
    fun finishTransition() {
        finishAfterTransition()
    }

}