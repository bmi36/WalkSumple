package com.example.walksumple

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "entity")
data class StepEntity(
    @PrimaryKey
    @ColumnInfo(name = "data")
    val id: Long,

    @ColumnInfo(name = "step")
    var step: Int

):Serializable