package com.example.walksumple

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mStepDetectorSensor: Sensor
    private lateinit var mStepCanterSensor: Sensor

    private var stepCounter: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var viewModel: SteoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[SteoViewModel::class.java]
        dayFlg.execute()
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        mStepCanterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val dayFlg = DayilyEventController(0, 0)

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) { Sensor.TYPE_STEP_DETECTOR -> stepCounter++ }

        stepsValue.text = (stepCounter).toString()
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
        if (!dayFlg.isDoneDaily()) {
            prefs.run {
                val day = Calendar.getInstance()
                val step = getInt("walk", 0)
                viewModel.UandI(StepEntity(day.timeInMillis,step))
                stepCounter = 0
                edit().clear().apply()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        stepCounter = prefs.getInt("walk", 0)
    }

}

fun Long.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.US).format(this.toInt() / 1000000).toLong()

fun Date.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.US).format(this).toLong()