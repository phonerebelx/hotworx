package com.hotworx.models.BrivoDataModels.BrivoLocation

data class AccessPoint(
    val bluetoothReader: BluetoothReader,
    val id: Int,
    val isDoorStation: Boolean,
    val name: String,
    val schedules: List<Schedule>,
    val twoFactorEnabled: Boolean,
    val type: String
)