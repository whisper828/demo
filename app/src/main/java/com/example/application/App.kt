package com.example.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.drake.brv.utils.BRV
import com.drake.logcat.LogCat
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.interfaces.NetErrorHandler
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setErrorHandler
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.statelayout.StateConfig
import com.drake.tooltip.dialog.BubbleDialog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.example.application.ext.app
import com.example.application.http.OAHttpApi
import com.example.application.utils.GsonConverter
import com.example.application.utils.versionCode
import java.util.*
import java.util.concurrent.TimeUnit


open class App : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        // 初始化BindingAdapter的默认绑定ID
        BRV.modelId = BR.m

        //网络请求初始化配置
        NetConfig.init(OAHttpApi.BASE_HOST) {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            setErrorHandler(object : NetErrorHandler {
                override fun onError(e: Throwable) {
                    super.onError(e)
                    LogCat.i(e.message)
                }

                override fun onStateError(e: Throwable, view: View) {
                    super.onStateError(e, view)
                    LogCat.i(e.message)
                }
            })

            setConverter(GsonConverter())

            setDialogFactory {
                BubbleDialog(it, "加载中...")
            }

            setRequestInterceptor(object : RequestInterceptor { // 添加请求拦截器
                override fun interceptor(request: BaseRequest) {
                    request.addHeader("App-version", versionCode().toString())
                    request.setHeader("Call-Source", "android")
                }
            })
            addInterceptor(LogRecordInterceptor(true)) // 添加日志记录器
        }

        StateConfig.apply {
            loadingLayout = R.layout.layout_loading
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
        }

        StateConfig.setRetryIds(R.id.root)//缺省页点击空白处重新加载

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }


    }

}