package com.example.application.activity

import android.os.Bundle
import com.drake.net.utils.runMain
import com.drake.serialize.intent.openActivity
import com.example.application.R
import com.example.application.base.BaseActivity
import com.example.application.databinding.ActivitySplashBinding
import com.example.application.utils.StatusBar
import java.util.*
import kotlin.concurrent.schedule

/**
 * Created by YeYun on 2021/10/15
 * 引导页
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
    }

    override fun initView() {
        StatusBar.hideStatusBar(this)
    }

    override fun initData() {
        startAnimAndWait()
    }

    //开启动画   三秒后进入启动页面或主界面
    private fun startAnimAndWait() {
//            等待三秒  进入滑动页面
        Timer().schedule(3000) {
            runMain {
                openActivity<MainActivity>()
                finishTransition()
            }
        }
    }

}