package com.example.application.data

import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize

/**
 * @date 2023/9/26
 * @Created by YeYun
 * @description
 */
object GlobalModel {

    var taskDataSource: String
        get() = deserialize("task_data_sources", "")
        set(value) {
            serialize("task_data_sources" to value)
        }

}