package com.example.c323p9selfieaday

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

        //sensor for accelerator/ shake
class AccelerometerSensor(context: Context):
AndroidSensor(context = context,
    sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER)