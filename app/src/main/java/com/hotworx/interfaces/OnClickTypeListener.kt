package com.hotworx.interfaces

import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetShowSlotDataModelItem

interface OnClickTypeListener {
    fun onItemClick(data: GetShowSlotDataModelItem, type: String)
}