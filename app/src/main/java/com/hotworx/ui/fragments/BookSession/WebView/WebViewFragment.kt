package com.hotworx.ui.fragments.BookSession.WebView

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.databinding.FragmentWebViewBinding
import com.hotworx.interfaces.BookingConfirmationDialogClickListener

class WebViewFragment(
    val locationName: String,
    val url: String,
    val clickListener: BookingConfirmationDialogClickListener,
    val dockActivity: DockActivity
) : BottomSheetDialogFragment() {

    lateinit var binding: FragmentWebViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        setUI()
        openWebView()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            (bottomSheet.layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(0, 0, 0, 0)
            bottomSheet.requestLayout()
            (dialog as? BottomSheetDialog)?.behavior?.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
            }
        }
    }

    private fun setUI() {
        binding.tvLocationName.text = locationName
    }
    private fun openWebView() {

        val webSettings: WebSettings = binding.wvPayment.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        binding.wvPayment.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                if (url.contains("status=success")) {
                    clickListener.onConfirmBooking()
                    dialog?.dismiss()
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        binding.wvPayment.webChromeClient = WebChromeClient()

        binding.wvPayment.loadUrl(url)
    }

}
