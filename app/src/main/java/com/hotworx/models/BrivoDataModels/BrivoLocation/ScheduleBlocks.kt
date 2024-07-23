package com.hotworx.models.BrivoDataModels.BrivoLocation



data class ScheduleBlocks(
    val friday: List<Friday>,
    val holiday: List<Holiday>,
    val monday: List<Monday>,
    val saturday: List<Saturday>,
    val sunday: List<Sunday>,
    val thursday: List<Thursday>,
    val tuesday: List<Tuesday>,
    val wednesday: List<Wednesday>
)