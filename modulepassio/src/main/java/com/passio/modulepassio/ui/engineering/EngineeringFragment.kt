package com.passio.modulepassio.ui.engineering

import com.passio.modulepassio.ui.engineering.EngineeringViewModel
import com.passio.modulepassio.databinding.FragmentEngineeringBinding
import com.passio.modulepassio.ui.base.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EngineeringFragment : BaseFragment<EngineeringViewModel>() {

    private var _binding: FragmentEngineeringBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEngineeringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
//            layoutTop3.setOnClickListener {
//                router.navigateToTop3()
//            }
//            layoutCameraPhotos.setOnClickListener {
//                router.navigateToImageLocal()
//            }
//            layoutBarcode.setOnClickListener {
//                router.navigateToBarcode()
//            }
//            layoutOCR.setOnClickListener {
//                router.navigateToOCR()
//            }
//            layoutNutritionFacts.setOnClickListener {
//                router.navigateToNutritionFacts()
//            }
//            layoutAdvisorVoice.setOnClickListener {
//                router.navigateToVoice()
//            }
//            layoutAdvisorImage.setOnClickListener {
//                router.navigateToImageRemote()
//            }
        }
    }
}