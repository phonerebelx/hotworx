package com.hotworx.interfaces

import com.hotsquad.hotsquadlist.network.ApiResponseCallback

interface GetStringOnClickListener {
   fun onClick(data: String,type:String)
}

interface BookingConfirmationDialogClickListener {
   fun onConfirmBooking(isSuccess: Boolean)
}