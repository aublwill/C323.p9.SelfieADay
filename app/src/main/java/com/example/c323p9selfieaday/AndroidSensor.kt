package com.example.c323p9selfieaday

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.google.android.gms.common.Feature

abstract class AndroidSensor(
    //sensor
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
): MeasurableSensor(sensorType), SensorEventListener{
    /*
    checks to see if sensor exists
     */
    override val doesSensorExist:Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    //sensor variables
    private lateinit var sensorManager:SensorManager
    private var sensor: Sensor? = null

    /*
    begins listening to sensors
     */
    override fun startListening(){
        if (!doesSensorExist)
            return
        if (!::sensorManager.isInitialized && sensor==null){
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    /*
    stops listening to sensors
     */
    override fun stopListening(){
        if (!doesSensorExist || !::sensorManager.isInitialized) {
            return
        }
        sensorManager.unregisterListener(this)
    }

    /*
    detects change in sensors
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (!doesSensorExist) {
            return
        }
        if (event?.sensor?.type == sensorType) {
            onSensorValuesChanged?.invoke(event.values.toList())
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
}
//abstract class for sensor
abstract class MeasurableSensor(protected val sensorType: Int) {
    protected var onSensorValuesChanged: ((List<Float>) -> Unit)? = null
    abstract val doesSensorExist: Boolean
    abstract fun startListening()
    abstract fun stopListening()
    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValuesChanged = listener
    }
}