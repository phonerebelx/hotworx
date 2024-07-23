package com.hotworx.models.BrivoDataModels.BrivoLocation



data class Schedule(
    val description: String,
    val holidays: List<Any>,
    val id: Int,
    val name: String,
    val scheduleBlocks: ScheduleBlocks,
    val scheduleExceptions: List<Any>
)