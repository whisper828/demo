package com.example.application.data

/**
 * @date 2023/9/26
 * @Created by YeYun
 * @description 子任务模型
 */

data class TaskDecomposeModel(
    var id: Long = 0,
    var title: String = "",
    var taskTime: String = "",
    var taskDate: String = "",
    var taskType: Int = 0,
    var taskRing: Int = 0,
    var checked: Boolean = false
){
    fun isEmpty(): Boolean{
        return title.isEmpty() || taskDate.isEmpty() || taskTime.isEmpty()
    }
}
