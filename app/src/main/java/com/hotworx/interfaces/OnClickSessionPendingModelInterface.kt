package com.hotworx.interfaces

import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse

interface OnClickSessionPendingModelInterface {
    fun onItemClick(value: PendingSessionResponse.SquadInvitation, type: String)
    fun onItemClickDecline(value: PendingSessionResponse.SquadInvitation, type: String)
}