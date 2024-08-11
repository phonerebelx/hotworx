package com.hotworx.ui.dialog.ReferralQr

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.Extensions.shareBitmap
import com.hotworx.Extensions.shareLink
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentQrDialogBinding
import com.hotworx.helpers.Utils
import com.hotworx.models.ReferralQr.ReferralQrDataModel
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QrDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentQrDialogBinding
    lateinit var qrModel: ReferralQrDataModel
    lateinit var dockActivity: DockActivity
    lateinit var generateQrBitmap: Bitmap
    lateinit var getContext: Context
    var checkComeFromBusinessCard: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrDialogBinding.inflate(layoutInflater)
        updateUIForBusiness()
        setQrCode()
        setData()
        setOnClickListener()
        return binding.root
    }


    private fun updateUIForBusiness(){
        if (checkComeFromBusinessCard){
            val params1 = binding.fl1.layoutParams as LinearLayout.LayoutParams
            params1.weight = 0.5f
            binding.fl1.layoutParams = params1


            val params2 = binding.fl2.layoutParams as LinearLayout.LayoutParams
            params2.weight = 0.5f
            binding.fl2.layoutParams = params1
            binding.fl2.visibility = View.VISIBLE
        }
    }
 
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), com.hotworx.R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun setContext(context: Context){
        getContext = context
    }

    private fun setData() {
        binding.tvName.text = qrModel.name
        binding.tvEmail.text = qrModel.email
    }

    private fun setQrCode() {
        generateQrBitmap = generateQr(requireContext())
        binding.ivQrCode.setImageBitmap(generateQrBitmap)
    }

    private fun generateQr(context: Context): Bitmap {
        val data = QrData.Url(qrModel.url)
        val options = createQrVectorOptions {
            background {
                drawable = ContextCompat.getDrawable(context, com.hotworx.R.drawable.white_background)
            }
            logo {
                drawable = ContextCompat.getDrawable(context, com.hotworx.R.drawable.qr_logo_rounded)
                size = .24f
                padding = QrVectorLogoPadding.Natural(.2f)
                shape = QrVectorLogoShape.Circle
            }
            colors {
                dark = QrVectorColor.LinearGradient(
                    colors = listOf(
                        0f to ContextCompat.getColor(context, com.hotworx.R.color.colorAccent),
                        1f to ContextCompat.getColor(context, com.hotworx.R.color.colorRed)
                    ),
                    orientation = QrVectorColor.LinearGradient.Orientation.Vertical
                )
                ball = QrVectorColor.Solid(ContextCompat.getColor(context, com.hotworx.R.color.colorRed))
                frame = QrVectorColor.LinearGradient(
                    colors = listOf(
                        0f to ContextCompat.getColor(context, com.hotworx.R.color.colorAccent),
                        1f to ContextCompat.getColor(context, com.hotworx.R.color.colorRed)
                    ),
                    orientation = QrVectorColor.LinearGradient.Orientation.Vertical
                )
            }
            shapes {
                darkPixel = QrVectorPixelShape.Circle(0.75f)
                ball = QrVectorBallShape.RoundCorners(.5f)
                frame = QrVectorFrameShape.RoundCorners(.5f)
            }
        }

        val qrCodeDrawable = QrCodeDrawable(data, options)
        val desiredWidth = 300
        val desiredHeight = 300
        val bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(ContextCompat.getColor(context, android.R.color.white))
        qrCodeDrawable.setBounds(0, 0, desiredWidth, desiredHeight)
        qrCodeDrawable.draw(canvas)
        return bitmap
    }

    private fun setOnClickListener() {
        binding.let {
            it.btnSave.setOnClickListener {
                if (::generateQrBitmap.isInitialized) {
                    if (checkAndRequestPermissions()) {
                        val result = saveBitmapToGallery(generateQrBitmap, requireContext())
                        if (result) {
                            dockActivity.showSuccessMessage("QR Code saved successfully")
                        } else {
                            dockActivity.showErrorMessage("QR Code not saved successfully")
                        }
                    } else {
                        dockActivity.showErrorMessage("QR Code not saved successfully \n Permission not granted")
                    }
                }
            }

            it.btnShare.setOnClickListener {
                if (::generateQrBitmap.isInitialized) {

                   getContext.shareLink(qrModel.url)
                    Log.d("vjhvvdschjd",qrModel.url)

                }
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             return true
        }
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    private fun saveBitmapToGallery(bitmap: Bitmap, context: Context): Boolean {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "QR_$timestamp.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return uri?.let {
            try {
                val outputStream: OutputStream? = resolver.openOutputStream(it)
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        } ?: false
    }
}
