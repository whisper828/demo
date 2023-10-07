package com.example.application.ext

import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.drake.net.reflect.TypeToken
import com.example.application.data.GlobalModel
import com.example.application.data.TaskDecomposeModel

/**
 * @date 2023/9/26
 * @Created by YeYun
 * @description
 */

fun addTask(decomposeModel: TaskDecomposeModel){
    val dataSource = GlobalModel.taskDataSource
    if (dataSource.isEmpty()){
        val result = JSONObject.toJSONString(mutableListOf(decomposeModel))
        GlobalModel.taskDataSource = result
    }else{
        val data: MutableList<TaskDecomposeModel> = GsonUtils.fromJson(dataSource, object : TypeToken<List<TaskDecomposeModel>>() {}.getType())
        data.add(decomposeModel)
        val result = JSONObject.toJSONString(data)
        GlobalModel.taskDataSource = result
    }
}

fun getAllTask(): MutableList<TaskDecomposeModel>{
    val dataSource = GlobalModel.taskDataSource
    return if (dataSource.isEmpty()){
        mutableListOf()
    }else{
        val data: MutableList<TaskDecomposeModel> = GsonUtils.fromJson(dataSource, object : TypeToken<List<TaskDecomposeModel>>() {}.getType())
        data.reverse()
        data
    }
}

fun getTodayTask(): MutableList<TaskDecomposeModel>{
    val dataSource = GlobalModel.taskDataSource
    val data = mutableListOf<TaskDecomposeModel>()
    return if (dataSource.isEmpty()){
        data
    }else{
        val tasks: MutableList<TaskDecomposeModel> = GsonUtils.fromJson(dataSource, object : TypeToken<List<TaskDecomposeModel>>() {}.getType())
        for (element in tasks){
            val taskDate = "${element.taskDate} ${element.taskTime}:00"
            if (TimeUtils.isToday(TimeUtils.string2Date(taskDate))){
                data.add(element)
            }
        }
        data.reverse()
        data
    }
}

fun deleteTask(taskDecomposeModel: TaskDecomposeModel){
    val dataSource = GlobalModel.taskDataSource
    if (dataSource.isEmpty()){
        return
    }else{
        val data: MutableList<TaskDecomposeModel> = GsonUtils.fromJson(dataSource, object : TypeToken<List<TaskDecomposeModel>>() {}.getType())
        var positon = -1
        for ((index, element) in data.withIndex()){
            if (element.id == taskDecomposeModel.id){
                positon = index
            }
        }
        if (positon >= 0){
            data.removeAt(positon)
        }
        val result = JSONObject.toJSONString(data)
        GlobalModel.taskDataSource = result
    }
}

fun getDataSources(): List<TaskDecomposeModel>{
    val dataSource = GlobalModel.taskDataSource
    return if (dataSource.isEmpty()){
        mutableListOf()
    }else {
        GsonUtils.fromJson<MutableList<TaskDecomposeModel>?>(
            dataSource,
            object : TypeToken<List<TaskDecomposeModel>>() {}.getType()
        )
    }
}