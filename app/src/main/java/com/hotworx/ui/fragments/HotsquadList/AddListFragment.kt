package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentAddListBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.CreateHotsquadModel
import com.hotworx.models.HotsquadList.HotsquadListModel
import com.hotworx.models.HotsquadList.UserModel
import com.hotworx.models.UserData.GetUserGoalData
import com.hotworx.models.UserData.ResponseUserGoalModel
import com.hotworx.models.UserData.ResponseUserProfileModel
import com.hotworx.models.UserData.getUserData
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddListFragment : BaseFragment() , LoadingListener {
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

//    private fun createHotSquadList(){
//        onLoadingStarted()
//        webService?.createHotsquadList(
//            ApiHeaderSingleton.apiHeader(requireContext()),
//            binding.titleEt.text.toString(),
//            binding.descriptionEt.text.toString()
//        )?.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                onLoadingFinished()
//                try {
//                    if (response.code() == 200 && response.body() != null) {
//                        val myHotsquadListFragment = MyHotsquadListFragment()
//                        dockActivity.replaceDockableFragment(myHotsquadListFragment)
//                    }
//                } catch (ex: Exception) {
//                    Utils.customToast(requireContext(), resources.getString(R.string.internal_exception_messsage))
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                onLoadingFinished()
//                Utils.customToast(requireContext(), t.toString())
//            }
//        })
//    }

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
                onLoadingStarted()
                try {
                    val response = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, CreateHotsquadModel::class.java)!!
                    if (response.message == "success"){
                        val myHotsquadListFragment = MyHotsquadListFragment()
                        dockActivity.replaceDockableFragment(myHotsquadListFragment)
                        onLoadingFinished()
                    }else{
                        onLoadingFinished()
                        dockActivity?.showErrorMessage("Something Went Wrong")
                    }

                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    onLoadingFinished()
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

    override fun onLoadingStarted() {
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onLoadingFinished() {
        isLoading = false
        binding.progressBar.visibility = View.GONE
    }

    override fun onProgressUpdated(percentLoaded: Int) {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
