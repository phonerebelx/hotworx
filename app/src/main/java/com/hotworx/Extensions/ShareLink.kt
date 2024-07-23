package com.hotworx.Extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun Context.shareLink(url: String){

    val sendIntent = Intent(
        Intent.ACTION_SEND
    ).apply {
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(
        sendIntent,"Share Referral Link"
    )

    startActivity(shareIntent)
}


fun Context.shareBitmap(bitmap: Bitmap) {
    var tempFile: File? = null
    try {
        tempFile = File.createTempFile("referral_qr_", ".jpg", externalCacheDir)
        val outputStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return
    }


    val imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", tempFile)
    Log.d( "shareBitmap: ",imageUri.toString())
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(shareIntent)
}
