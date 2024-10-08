package com.example.passiomodulenew.ui.camera

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.example.passiomodulenew.data.SharedPrefUtils
import com.passio.passiomodulenew.databinding.ScanInfoLayoutBinding

class ScanInfoDialog(context: Context) : Dialog(context) {

    private lateinit var binding: ScanInfoLayoutBinding

    companion object{
        fun show(context: Context, isForceShow: Boolean = false)
        {
            val isAlreadyShownOnce = SharedPrefUtils.get("WayToScanInfoShown", Boolean::class.java)
            if (!isAlreadyShownOnce) {
                SharedPrefUtils.put("WayToScanInfoShown", true)
                ScanInfoDialog(context).show()
            }
            else if (isForceShow)
            {
                ScanInfoDialog(context).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = ScanInfoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.ok.setOnClickListener {
            dismiss()
        }

    }

}