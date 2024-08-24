package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentAddListBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.CreateHotsquadModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class AddListFragment : BaseFragment(){
    private var _binding: FragmentAddListBinding? = null
    private val binding get() = _binding!!
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddList.setOnClickListener {
            val inputTitle = binding.titleEt.text.toString()
            val inputDesc = binding.descriptionEt.text.toString()
            if (TextUtils.isEmpty(inputTitle)) {
                binding.titleEt.error = "Field Required!!"
                binding.titleEt.requestFocus()
            } else if (TextUtils.isEmpty(inputDesc)) {
                binding.descriptionEt.error = "Field Required!!"
                binding.descriptionEt.requestFocus()
            } else {
                //createHotSquadList()
                callApi(Constants.CREATE_SQUADLIST, "")
            }
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
    }

    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.CREATE_SQUADLIST -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().createHotsquadList(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        binding.titleEt.text.toString(),
                        binding.descriptionEt.text.toString(),
                    ), Constants.CREATE_SQUADLIST, true
                )

            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.CREATE_SQUADLIST -> {
                try {
                    val response = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, CreateHotsquadModel::class.java)!!
                    if (response.status){
                        val myHotsquadListFragment = MyHotsquadListFragment()
                        dockActivity.removeDockableFragment(myHotsquadListFragment)
                    }else{
                        dockActivity?.showErrorMessage("Something Went Wrong")
                    }
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {
        myDockActivity?.showErrorMessage(message)
        Log.i("xxError", "Error")
    }

    override fun onBackPressed() {
        super.onBackPressed() // If not loading, proceed with the default back button action
        if (isLoading) {
            Utils.customToast(requireContext(), getString(R.string.message_wait));
        }else{
//            backBtnWork()
//            btnBack
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // unbinder.unbind();
    }
}
