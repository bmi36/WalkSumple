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

    private  var mSensorManager: SensorManager? = null
    private var mStepDetectorSensor: Sensor? = null
    private var mStepConterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        mStepConterSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}

    override fun onSensorChanged(event: SensorEvent) {
        val sensor = event.sensor
        val value = event.values
        val teimeStamp = event.timestamp

        if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
            Log.d("test", value[0].toString())
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mStepConterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        mSensorManager?.registerListener(this,mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStop() {
        super.onStop()

        mSensorManager?.unregisterListener(this,mStepConterSensor)
        mSensorManager?.unregisterListener(this,mStepDetectorSensor)
    }
}
