package com.passio.passiomodule.ui.camera

import com.passio.passiomodule.R
import com.passio.passiomodule.data.ResultWrapper
import com.passio.passiomodule.databinding.FragmentCameraRecognitionBinding
import com.passio.passiomodule.domain.camera.RecognitionResult
import com.passio.passiomodule.ui.base.BaseFragment
import com.passio.passiomodule.ui.base.BaseToolbar
import com.passio.passiomodule.ui.model.FoodRecord
import com.passio.passiomodule.ui.util.ProgressDialog
import ai.passio.passiosdk.core.camera.PassioCameraViewProvider
import ai.passio.passiosdk.passiofood.DetectedCandidate
import ai.passio.passiosdk.passiofood.data.model.PassioFoodItem
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CameraRecognitionFragment : BaseFragment<CameraRecognitionViewModel>(),
    PassioCameraViewProvider,
    BaseToolbar.ToolbarListener, View.OnClickListener {

    private var _binding: FragmentCameraRecognitionBinding? = null
    private val binding: FragmentCameraRecognitionBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraRecognitionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recognitionResult.visibility = View.GONE
        viewModel.recognitionResults.observe(viewLifecycleOwner, ::onRecognitionResult)
        viewModel.scanModeEvent.observe(viewLifecycleOwner, ::scanModeUpdated)
        viewModel.foodItemResult.observe(viewLifecycleOwner, ::editFoodItem)
        viewModel.logFoodEvent.observe(viewLifecycleOwner, ::foodItemLogged)
        viewModel.showLoading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                ProgressDialog.show(requireContext())
            } else {
                ProgressDialog.hide()
            }
        }
//        setupToolbar()
        initOnClickCallback()

        binding.recognitionResult.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        binding.recognitionResult.addBottomSheetCallback(bottomSheetCallback)
        binding.recognitionResult.setRecognitionResultListener(recognitionResultListener)

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request permission
            requestCameraPermission()
        } else {
            // Permission is already granted
            cameraPermissionGranted()
        }
    }

    private fun initOnClickCallback() {
        with(binding)
        {
            foodsLabel.setOnClickListener(this@CameraRecognitionFragment)
            barcodeLabel.setOnClickListener(this@CameraRecognitionFragment)
            nutritionFactsLabel.setOnClickListener(this@CameraRecognitionFragment)
            keepScanning.setOnClickListener(this@CameraRecognitionFragment)
            viewDiary.setOnClickListener(this@CameraRecognitionFragment)
        }
    }

    private fun foodItemLogged(resultWrapper: ResultWrapper<Boolean>) {
        when (resultWrapper) {
            is ResultWrapper.Success -> {
                if (resultWrapper.value) {
                    with(binding)
                    {
                        recognitionResult.visibility = View.GONE
                        scanningMessage.visibility = View.GONE
                        viewAddedToDiary.visibility = View.VISIBLE
                        recognitionResult.reset()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not log food item.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is ResultWrapper.Error -> {
                Toast.makeText(
                    requireContext(),
                    resultWrapper.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun editFoodItem(resultWrapper: ResultWrapper<PassioFoodItem>) {

        when (resultWrapper) {
            is ResultWrapper.Success -> {
                sharedViewModel.editFoodRecord(FoodRecord(resultWrapper.value))
                viewModel.navigateToEdit()
            }

            is ResultWrapper.Error -> {
                Toast.makeText(
                    requireContext(),
                    resultWrapper.error,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private val recognitionResultListener = object :
        RecognitionResultView.RecognitionResultListener {
        override fun onLogVisual(detectedCandidate: DetectedCandidate) {
            viewModel.stopDetection()
            with(binding) {
                viewModel.logFood(detectedCandidate.passioID)
                /*recognitionResult.visibility = View.GONE
                scanningMessage.visibility = View.GONE
                viewAddedToDiary.visibility = View.VISIBLE
                recognitionResult.reset()*/
            }
        }

        override fun onEditVisual(detectedCandidate: DetectedCandidate) {
            viewModel.stopDetection()
            with(binding) {
//                recognitionResult.visibility = View.GONE
//                scanningMessage.visibility = View.GONE
//                viewAddedToDiary.visibility = View.VISIBLE
//                recognitionResult.reset()

                viewModel.fetchFoodItemToEdit(detectedCandidate.passioID)
            }
        }

        override fun onEditProduct(result: RecognitionResult.ProductRecognition) {
            viewModel.stopDetection()
            with(binding) {
//                recognitionResult.visibility = View.GONE
//                scanningMessage.visibility = View.GONE
//                viewAddedToDiary.visibility = View.VISIBLE
//                recognitionResult.reset()
                editFoodItem(ResultWrapper.Success(result.foodItem))
            }
        }

        override fun onLogProduct(result: RecognitionResult.ProductRecognition) {
            viewModel.stopDetection()
            with(binding) {
                viewModel.logFood(result.foodItem)
                /*recognitionResult.visibility = View.GONE
                scanningMessage.visibility = View.GONE
                viewAddedToDiary.visibility = View.VISIBLE
                recognitionResult.reset()*/
            }
        }

    }

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            Log.d("MMMM", "NEW STATE: $newState")
            if (newState == STATE_COLLAPSED) {
                viewModel.startOrUpdateDetection()
            } else {
                viewModel.stopDetection()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            Log.d("MMMM", "ON SLIDE: $slideOffset")
        }
    }

    private var highlightJob: Job? = null
    private fun highlightScanMode(str: String) {
        highlightJob?.cancel()
        highlightJob = lifecycleScope.launch {
            binding.tvScanModeHighlight.text = str
            binding.tvScanModeHighlight.isVisible = true
            delay(2000)
            binding.tvScanModeHighlight.isVisible = false
        }


    }

    private fun scanModeUpdated(scanMode: ScanMode) {
        when (scanMode) {
            ScanMode.VISUAL -> {
                binding.foodsLabel.setImageResource(R.drawable.icon_foods)
                binding.barcodeLabel.setImageResource(R.drawable.icon_barcode_disabled)
                binding.nutritionFactsLabel.setImageResource(R.drawable.icon_nutrition_facts_disabled)
                binding.tvProgressInfo.text = "Place your food within the frame."
                highlightScanMode(resources.getString(R.string.foods))
            }

            ScanMode.BARCODE -> {
                binding.foodsLabel.setImageResource(R.drawable.icon_foods_disabled)
                binding.barcodeLabel.setImageResource(R.drawable.icon_barcode)
                binding.nutritionFactsLabel.setImageResource(R.drawable.icon_nutrition_facts_disabled)
                binding.tvProgressInfo.text = "Place your barcode within the frame."
                highlightScanMode(resources.getString(R.string.barcode_mode))
            }

            ScanMode.NUTRITION_FACTS -> {
                binding.foodsLabel.setImageResource(R.drawable.icon_foods_disabled)
                binding.barcodeLabel.setImageResource(R.drawable.icon_barcode_disabled)
                binding.nutritionFactsLabel.setImageResource(R.drawable.icon_nutrition_facts)
                binding.tvProgressInfo.text = "Place the nutrition facts within the frame."
                highlightScanMode(resources.getString(R.string.nutrition_facts_mode))
            }

        }

    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setup(getString(R.string.food_scanner), this@CameraRecognitionFragment)
            setRightIcon(R.drawable.ic_info)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted
            cameraPermissionGranted()
        } else {
            // Permission is denied
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Permission denied without "Don't ask again"
                showPermissionDeniedMessage()
            } else {
                // Permission denied with "Don't ask again"
                showPermissionDeniedPermanentlyMessage()
            }
        }
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun cameraPermissionGranted() {
        // Your code to start the camera
        ScanInfoDialog.show(requireContext())
    }

    private fun showPermissionDeniedMessage() {
        // Show a message explaining why the permission is needed
        AlertDialog.Builder(requireContext())
            .setTitle("Permission needed")
            .setMessage("Camera permission is needed to access this feature.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                requestCameraPermission()
            }
            .show()
    }

    private fun showPermissionDeniedPermanentlyMessage() {
        // Show a message guiding the user to the app settings
        AlertDialog.Builder(requireContext())
            .setTitle("Permission needed")
            .setMessage("Camera permission is needed to access this feature. Please enable it in the app settings.")
            .setPositiveButton("Open Settings") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    override fun onStart() {
        super.onStart()
        viewModel.startRecognitionSession(this)
    }

    override fun onStop() {
        viewModel.stopRecognitionSession()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.recognitionResult.removeBottomSheetCallback(bottomSheetCallback)
        _binding = null
        super.onDestroyView()
    }

    override fun requestCameraLifecycleOwner(): LifecycleOwner = this

    override fun requestPreviewView(): PreviewView = binding.cameraPreview

    private fun onRecognitionResult(result: RecognitionResult) {
        binding.let {
            when (result) {
                RecognitionResult.NoProductRecognition -> {
                    it.viewAddedToDiary.visibility = View.GONE
                    it.recognitionResult.visibility = View.GONE
                    it.scanningMessage.visibility = View.VISIBLE
                    it.recognitionResult.reset()
                }

                RecognitionResult.NoRecognition -> {
                    it.viewAddedToDiary.visibility = View.GONE
                    it.recognitionResult.visibility = View.GONE
                    it.scanningMessage.visibility = View.VISIBLE
                    it.recognitionResult.reset()
                }

                is RecognitionResult.ProductRecognition -> {
                    it.viewAddedToDiary.visibility = View.GONE
                    it.recognitionResult.visibility = View.VISIBLE
                    it.scanningMessage.visibility = View.GONE

                    it.recognitionResult.showProductResult(result)
                }

                is RecognitionResult.VisualRecognition -> {
                    it.viewAddedToDiary.visibility = View.GONE
                    it.recognitionResult.visibility = View.VISIBLE
                    it.scanningMessage.visibility = View.GONE

                    it.recognitionResult.showVisualResult(result)
                }
            }
        }
    }

    override fun onBack() {
        viewModel.navigateBack()
    }

    override fun onRightIconClicked() {
        ScanInfoDialog.show(requireContext(), true)
    }

    override fun onClick(p0: View?) {
        when (p0) {

            binding.foodsLabel -> {
                viewModel.setFoodScanMode(ScanMode.VISUAL)
            }

            binding.barcodeLabel -> {
                viewModel.setFoodScanMode(ScanMode.BARCODE)
            }

            binding.nutritionFactsLabel -> {
                viewModel.setFoodScanMode(ScanMode.NUTRITION_FACTS)
            }

            binding.keepScanning -> {
                viewModel.startOrUpdateDetection()
            }

            binding.viewDiary -> {
                viewModel.navigateToDiary()
            }

        }
    }
}