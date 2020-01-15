package com.example.walksumple

import android.content.Context
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

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mStepDetectorSensor: Sensor? = null
    private var mStepConterSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager?.getDefaultSensor(TYPE_STEP_DETECTOR)
        mStepConterSensor = mSensorManager?.getDefaultSensor(TYPE_STEP_COUNTER)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private var stepCounter: Int = 0
    private var detetorCounter: Int = 0
    private var timeStamp: Long? = null
    private val dayFlg = DayilyEventController(0,0)

    override fun onSensorChanged(event: SensorEvent) {
        event.values.let {
            Log.d("test",it[0].toString())
            when (event.sensor.type) {
                TYPE_STEP_COUNTER -> stepCounter = it[0].toInt()

                TYPE_STEP_DETECTOR -> detetorCounter =
                    it[0].toInt().apply { stepsValue.text = toString() }
            }
            timeStamp = event.timestamp.toTypeDate()

            //データベースについか
            if (!dayFlg.isDoneDaily()){

                dayFlg.execute()

            }

        }
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
        stepsValue.text = stepCounter.toString()
        super.onStart()
    }
}

fun Long.toTypeDate() = SimpleDateFormat("yyyyMMdd").let { it.format(this.toInt() / 1000000).toLong() }
