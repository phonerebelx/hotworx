package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentHotsquadSearchBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.HotsquadList.CreateHotsquadModel
import com.hotworx.models.HotsquadList.UserModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.HotsquadListAdapter.UserListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HotsquadList.Bottomsheet.SearchUserBottomSheet
import com.hotworx.ui.views.TitleBar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class HotsquadSearchFragment : BaseFragment(){
    private var _binding: FragmentHotsquadSearchBinding? = null
    private val binding get() = _binding!!

    private val userList = mutableListOf<UserModel>()
    private lateinit var userListAdapter: UserListAdapter
    private var isLoading = false
    var resultString = ""
    var squadId = ""

    /**
     * Bottom Sheet
     */
    private lateinit var searchUserBottomSheet: SearchUserBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotsquadSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the ID from the arguments
        arguments?.let {
            squadId = it.getString("squad_id")?: ""
            Log.d("squadIDDDDD",squadId)
        }

        userListAdapter = UserListAdapter(userList, ::onDeleteItemClicked)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }

        binding.addUSer.setOnClickListener {
            val inputText = binding.titleEt.text.toString()
            if (TextUtils.isEmpty(inputText)) {
                binding.titleEt.error = "Field Required!!"
                binding.titleEt.requestFocus()
            } else if (!isValidEmailOrPhone(inputText)) {
                binding.titleEt.error = "Invalid Email or Phone Number!"
                binding.titleEt.requestFocus()
            } else {
                val userModel = UserModel(inputText)
                userList.add(userModel)
                userListAdapter.notifyItemInserted(userList.size - 1)
                binding.titleEt.text?.clear()
                updateSearchButtonVisibility()

                resultString = convertListToString(userList)
                Log.d("USERLISTTTTTTSep",resultString.toString())
            }
        }

        //Bottom Sheet
        searchUserBottomSheet = SearchUserBottomSheet()

        binding.btnSearchUser.setOnClickListener{
            callApi(Constants.SEARCH_SQUADLIST,"")
        }

        updateSearchButtonVisibility()
    }

    fun convertListToString(userList: List<UserModel>): String {
        val stringList = userList.map { "\"${it.searchedName}\"" } // Convert each item to a quoted string
        return stringList.joinToString(prefix = "[", postfix = "]", separator = ",\n")
    }

    private fun isValidEmailOrPhone(input: String): Boolean {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        val phonePattern = Pattern.compile("^\\+?[0-9]{10,13}\$")
        return emailPattern.matcher(input).matches() || phonePattern.matcher(input).matches()
    }

    private fun updateSearchButtonVisibility() {
        binding.btnSearchUser.visibility = if (userList.isNotEmpty()) View.VISIBLE else View.GONE    }

    private fun onDeleteItemClicked(position: Int) {
        if (position >= 0 && position < userList.size) {
            userList.removeAt(position)
            userListAdapter.notifyItemRemoved(position)
            userListAdapter.notifyItemRangeChanged(position, userList.size)

            updateSearchButtonVisibility()
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.leaderboard)
    }


    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.SEARCH_SQUADLIST -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().searchAddSquadMember(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        squadId,
                        resultString,
                    ), Constants.SEARCH_SQUADLIST, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.SEARCH_SQUADLIST -> {
                try {
                    val response = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, CreateHotsquadModel::class.java)!!
                    if (response.status){
                        val bundle = Bundle()
                        bundle.putSerializable("resultString", response.data.toString())
                        Log.d("resultString",resultString.toString())
                        searchUserBottomSheet.arguments = bundle
                        searchUserBottomSheet.show(parentFragmentManager, "TAG")
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

    //For BottomSheet
//    private fun addSquadMember(){
//        onLoadingStarted()
//        webService?.searchAddSquadMember(
//           resultString
//        )?.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                onLoadingFinished()
//                try {
//                    if (response.code() == 200 && response.body() != null) {
//                        val bundle = Bundle()
//                        bundle.putString("resultString", resultString)
////                        Log.d("resultString",resultString.toString())
//                        searchUserBottomSheet.arguments = bundle
//                        searchUserBottomSheet.show(parentFragmentManager, "TAG")
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

//    override fun onLoadingStarted() {
//        isLoading = true
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    override fun onLoadingFinished() {
//        isLoading = false
//        binding.progressBar.visibility = View.GONE
//    }
//
//    override fun onProgressUpdated(percentLoaded: Int) {
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}