package com.example.walksumple

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepDao{
    @Query("select sum(step) from entity where data like :data||'%' group by data||'%'")
    fun getsumSteps(data: Long):LiveData<Array<Int>>

    @Insert
    suspend fun insert(entity: StepEntity)

    @Update
    suspend fun update(entity: StepEntity)

    @Query("select sum(step) from entity where data between :year || 1 || '%' and :year || 12 || '%' group by data")
    fun getMonth(year: Long): LiveData<Array<Int>>

    @Query("select step from entity where data")
    fun getStep(): LiveData<Array<Int>>
}