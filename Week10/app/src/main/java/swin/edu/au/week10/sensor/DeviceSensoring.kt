package swin.edu.au.week10.sensor

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import swin.edu.au.week10.R

class DeviceSensoring :AppCompatActivity(), SensorEventListener {
    lateinit var mSensorManager: SensorManager
    var mSensors: Sensor? = null
    lateinit var txtSensorvalues : TextView


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sensor_view)
        txtSensorvalues = findViewById(R.id.txtSensorValues)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.v("Sensors", "Total Sensors: " + deviceSensors.size)
        deviceSensors.forEach(
            {
                Log.v("Sensors", it.name)
            }
        )

        mSensors = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        var milibarsOfPressure = event!!.values[0]
        if(event.sensor.type == Sensor.TYPE_PRESSURE){
            txtSensorvalues.text = milibarsOfPressure.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }
}