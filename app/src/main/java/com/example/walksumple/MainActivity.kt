package com.example.walksumple

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mStepDetectorSensor: Sensor? = null
    private var mStepConterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        mStepConterSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var step = 0

    override fun onSensorChanged(event: SensorEvent) {
        step = event.values.let { it[0].toInt() }
        stepsValue.text = step.toString()
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mStepConterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        mSensorManager?.registerListener(
            this,
            mStepDetectorSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onStop() {
        super.onStop()

        mSensorManager?.unregisterListener(this, mStepConterSensor)
        mSensorManager?.unregisterListener(this, mStepDetectorSensor)
    }

    override fun onStart() {
        stepsValue.text = step.toString()
        super.onStart()
    }
}
