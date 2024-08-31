package com.passio.passiomodule.ui.image

import com.passio.passiomodule.R
import com.passio.passiomodule.databinding.FragmentAddFoodBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.passio.passiomodule.ui.base.BaseFragment
import com.passio.passiomodule.ui.base.BaseViewModel
import com.passio.passiomodule.ui.menu.AddFoodAdapter
import com.passio.passiomodule.ui.menu.AddFoodFragmentDirections
import com.passio.passiomodule.ui.menu.AddFoodOption
import com.passio.passiomodule.ui.util.toast
import com.passio.passiomodule.ui.util.uriToBitmap
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TakeOrSelectPhotoFragment : BaseFragment<BaseViewModel>() {

    private val adapter = AddFoodAdapter(
        listOf(
            AddFoodOption(0, R.string.take_photos, R.drawable.ic_take_photo),
            AddFoodOption(1, R.string.select_photos, R.drawable.ic_select_photo),
        ),
        ::onOptionSelected
    )
    private lateinit var binding: FragmentAddFoodBinding

    private lateinit var pickImagesLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            addFoodList.layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = true
            }
            addFoodList.adapter = adapter

            buttonClose.setOnClickListener {
                viewModel.navigateBack()
            }
        }

        pickImagesLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    result.data?.clipData?.let { clipData ->
                        if (clipData.itemCount > TakePhotoFragment.MAX_IMAGES) {
                            requireContext().toast("You can only select up to ${TakePhotoFragment.MAX_IMAGES} images.")
                            return@registerForActivityResult
                        }
                        val uris = mutableListOf<Uri>()
                        for (i in 0 until clipData.itemCount) {
                            uris.add(clipData.getItemAt(i).uri)
                        }
                        navigateToFindResult(uris)
                    } ?: result.data?.data?.let {
                        navigateToFindResult(listOf(it))
                    }
                }
            }
    }

    private fun pickImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        pickImagesLauncher.launch(Intent.createChooser(intent, "Select Pictures"))
    }

    private fun navigateToFindResult(imageUris: List<Uri>) {
        lifecycleScope.launch(Dispatchers.IO)
        {
            val bitmaps = mutableListOf<Bitmap>()
            imageUris.forEach { uri ->
                val bitmap = uriToBitmap(requireContext(), uri)
                if (bitmap != null) {
                    bitmaps.add(bitmap)
                }
            }
            lifecycleScope.launch(Dispatchers.Default) {
                sharedViewModel.addPhotoFoodResult(bitmaps)
                viewModel.navigate(TakeOrSelectPhotoFragmentDirections.takeSelectPhotoToImageFoodResult())
            }
        }
    }

    private fun onOptionSelected(id: Int) {
        when (id) {
            0 -> viewModel.navigate(TakeOrSelectPhotoFragmentDirections.takeSelectPhotoToTakePhoto())
            1 -> pickImages()
        }
    }
}