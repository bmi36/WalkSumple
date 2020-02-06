package com.example.walksumple

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.sql.SQLException

class SteoViewModel(application: Application) : AndroidViewModel(application){
    private val repository: StepRepository = StepDataBase.getInstance(application).dao()
        .let { StepRepository(it) }

    val mstepArray: LiveData<Array<Int>>

    init {
        mstepArray = repository.getStep()
    }

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }

    fun UandI(entity: StepEntity) = try { insert(entity) }catch (e: SQLException){ update(entity) }
}