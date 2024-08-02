package com.hotsquad.hotsquadlist.listener

import android.content.DialogInterface

interface DialogListeners {

    /**
     * When user taps on positive button on dialog
     */
    fun onPositionButtonTap(dialog: DialogInterface?) {}


    /**
     * When user taps on Negative button on dialog
     */
    fun onNegativeButtonTap(dialog: DialogInterface?) {}
}