package com.hotworx.interfaces

import com.hotworx.models.DashboardData.TodaysPendingSession

interface OnClickPendingModelInterface {
    fun onItemClick(value: TodaysPendingSession, type: String)
}