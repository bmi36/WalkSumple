package com.example.walksumple

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_STEP_COUNTER
import android.hardware.Sensor.TYPE_STEP_DETECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    companion object {
        const val USER = "2"
        const val WALK = "1"
    }

    private lateinit var mSensorManager: SensorManager
    private lateinit var mStepDetectorSensor: Sensor
    private lateinit var mStepCanterSensor: Sensor

    private var stepCounter: Int = 0
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager.getDefaultSensor(TYPE_STEP_DETECTOR)
        mStepCanterSensor = mSensorManager.getDefaultSensor(TYPE_STEP_COUNTER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val dayFlg = DayilyEventController(0, 0)

    override fun onSensorChanged(event: SensorEvent) {
        event.values.let {
            Log.d("test", it[0].toString())

            stepsValue.text = (stepCounter).toString()

            if (!dayFlg.isDoneDaily()) {
                prefs.run {
                    val day = Date(System.currentTimeMillis()).toTypeDate()
                    val step = getInt(WALK, 0)
                    stepCounter = 0
                    edit().clear().apply()
                    dayFlg.execute()
//                    データベースに追加
                }
            }

            if (event.sensor.type == TYPE_STEP_COUNTER) { stepCounter++ }


        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mStepCanterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        mSensorManager.registerListener(
            this,
            mStepDetectorSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onStop() {
        super.onStop()
        prefs.edit().putInt("walk", stepCounter).apply()
        mSensorManager.unregisterListener(this, mStepCanterSensor)
        mSensorManager.unregisterListener(this, mStepDetectorSensor)
    }

    override fun onStart() {
        super.onStart()
        stepCounter = prefs.getInt("walk", 0)
        stepsValue.text = stepCounter.toString()
    }

}

fun Long.toTypeDate() =
    SimpleDateFormat("yyyyMMdd").let { it.format(this.toInt() / 1000000).toLong() }

fun Date.toTypeDate() =
    SimpleDateFormat("yyyyMMdd").let { it.format(this).toLong() }