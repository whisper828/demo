package com.example.application.data

import com.stx.xhb.androidx.entity.BaseBannerInfo

/**
 * @Date 2021/11/26
 * @Created by YeYun
 * @Description 首页 Banner 实体
 */
data class BannerModel(
    var id: String = "",
    var imgUrl: String = "",
    var localImageUrl: Int = 0,
    var title: String = "",
    var imgPath: String = "",
    var fileName: String = "",
    var sort: Int = 0,
) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return if(imgUrl.isNotEmpty()){
            imgUrl
        }else{
            localImageUrl
        }
    }

    override fun getXBannerTitle(): String {
        return fileName
    }
}