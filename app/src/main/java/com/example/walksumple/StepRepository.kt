package com.example.walksumple

import androidx.annotation.WorkerThread

class StepRepository (private val dao: StepDao){

    @WorkerThread
    suspend fun insert(entity: StepEntity) = dao.insert(entity)
    suspend fun update(entity: StepEntity) = dao.update(entity)
    fun getsum(date: Long) = dao.getsumSteps(date)
    fun getMonth(year: Long) = dao.getMonth(year)
    fun getStep() = dao.getStep()
}