package com.example.walksumple

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_STEP_DETECTOR
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mStepDetectorManager: Sensor
    private lateinit var mStemCanterSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mStepDetectorManager = mSensorManager.getDefaultSensor(TYPE_STEP_DETECTOR)

        mStemCanterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        accuracyに変更があったときの処理
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val sensor = event.sensor
            val values = event.values
            val timestamp = event.timestamp
            if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
                textView.text = values[0].toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(
            this,
            mStemCanterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        mSensorManager.registerListener(
            this,
            mStepDetectorManager,
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this,mStemCanterSensor)
        mSensorManager.unregisterListener(this,mStepDetectorManager)
    }
}
