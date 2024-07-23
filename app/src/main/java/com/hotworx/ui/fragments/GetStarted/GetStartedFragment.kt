package com.hotworx.ui.fragments.GetStarted

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.GettingStarted.GettingStartedModel.GettingStartedDataModel
import com.hotworx.models.GettingStarted.GettingStartedRequestModel
import com.hotworx.models.SessionBookingModel.SessionBookingDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.FrequentlyBooked.FrequentBookingAdapter
import com.hotworx.ui.adapters.GetStarted.GetStartedAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.models.GettingStarted.GettingStartedModel.Data
import com.hotworx.models.GettingStarted.GettingStartedModel.Video
import com.hotworx.ui.views.TitleBar
//import kotlinx.android.synthetic.main.fragment_get_started.rvGettingStartedAdapter

class GetStartedFragment : BaseFragment() {

    private lateinit var btnGetStart: Button
    private lateinit var rvGettingStartedAdapter: RecyclerView
    private lateinit var tvTutorials: TextView
    private lateinit var btnEquipTutorial: Button
    private lateinit var linearLayout: LinearLayout
    private lateinit var gettingStartedDataModel: GettingStartedDataModel
    private lateinit var gettingStartedAdapter: GetStartedAdapter
    private lateinit var whichApi: String
     var isHotworxVideos: Boolean = false
    companion object {
        fun newInstance(isHotworxVideos: Boolean): GetStartedFragment {

            val myFragment = GetStartedFragment()
            val args = Bundle()
            args.putBoolean("is_hotworx_videos", isHotworxVideos)
            myFragment.arguments = args
            return myFragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_get_started, container, false)

        btnGetStart = root.findViewById(R.id.btnGetStart)
        btnEquipTutorial = root.findViewById(R.id.btnEquipTutorial)
        tvTutorials = root.findViewById(R.id.tvTutorials)
        rvGettingStartedAdapter = root.findViewById(R.id.rvGettingStartedAdapter)
        linearLayout = root.findViewById(R.id.linearLayout)

        isHotworxVideos = arguments?.getBoolean("is_hotworx_videos") == true

        whichApi = "getstarted"

        if (isHotworxVideos){
            linearLayout.visibility = View.GONE
            tvTutorials.text = "HOTWORX on the GO"
            whichApi = "hah"
        }
        callApi(Constants.OTHER_VIDEOS,whichApi)
        setOnClickListener()
        return root

    }

    private fun callApi(type: String, data: String) {
        when (type) {
            Constants.OTHER_VIDEOS -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().other_videos(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        GettingStartedRequestModel(
                            data
                        )
                    ), Constants.OTHER_VIDEOS, true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.OTHER_VIDEOS -> {
                try {
                    gettingStartedDataModel =  GsonFactory.getConfiguredGson()?.fromJson(liveData.value, GettingStartedDataModel::class.java)!!
                    val gettingStartedArrayList: ArrayList<Video> = ArrayList(gettingStartedDataModel.data[0].videos)
                    initRecyclerView(gettingStartedArrayList)


                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                        dockActivity?.showErrorMessage(errorResponseEnt.error)
                    }
                }
            }

        }
    }

    override fun onFailure(message: String, tag: String) {
        super.onFailure(message, tag)
        gettingStartedAdapter = GetStartedAdapter(requireContext(),myDockActivity)
        gettingStartedAdapter.setList(arrayListOf())
        rvGettingStartedAdapter.adapter = gettingStartedAdapter
    }

    override fun ResponseNoInternet(tag: String){
        super.ResponseNoInternet(tag)
        gettingStartedAdapter = GetStartedAdapter(requireContext(),myDockActivity)
        gettingStartedAdapter.setList(arrayListOf())
        rvGettingStartedAdapter.adapter = gettingStartedAdapter
    }

    private fun initRecyclerView(gettingStartedVideosData: ArrayList<Video>){

        if (gettingStartedVideosData.size > 0) {
            gettingStartedAdapter = GetStartedAdapter(requireContext(),myDockActivity)
            gettingStartedAdapter.setList(gettingStartedVideosData)
            rvGettingStartedAdapter.adapter = gettingStartedAdapter
        }
    }

    private fun setOnClickListener() {
        btnGetStart.setOnClickListener {

//      set btnGetStart design acc to click
            btnGetStart.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            btnGetStart.setBackgroundResource(R.drawable.multicolor_background)
            val drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.icon_menu_getting_started_white
            )
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            btnGetStart.setCompoundDrawables(drawable, null, null, null)
            btnGetStart.compoundDrawablePadding = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)


//      set btnEquipTutorial design acc to click
            btnEquipTutorial.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            btnEquipTutorial.setBackgroundResource(`in`.aabhasjindal.otptextview.R.color.transparent)
            val drawable2 = ContextCompat.getDrawable(requireContext(), R.drawable.equipment_icon)
            drawable2?.setBounds(0, 0, drawable2.intrinsicWidth, drawable2.intrinsicHeight)
            btnEquipTutorial.setCompoundDrawables(drawable2, null, null, null)
            btnEquipTutorial.compoundDrawablePadding =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)

            callApi(Constants.OTHER_VIDEOS,"getstarted")
        }


        btnEquipTutorial.setOnClickListener {

            //      set btnGetStart design acc to click
            btnEquipTutorial.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            btnEquipTutorial.setBackgroundResource(R.drawable.multicolor_background)
            val drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.equipment_icon_white
            )
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            btnEquipTutorial.setCompoundDrawables(drawable, null, null, null)
            btnEquipTutorial.compoundDrawablePadding =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)


            //  set btnEquipTutorial design acc to click
            btnGetStart.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            btnGetStart.setBackgroundResource(`in`.aabhasjindal.otptextview.R.color.transparent)
            val drawable2 =
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_menu_getting_started)
            drawable2?.setBounds(0, 0, drawable2.intrinsicWidth, drawable2.intrinsicHeight)
            btnGetStart.setCompoundDrawables(drawable2, null, null, null)
            btnGetStart.compoundDrawablePadding =
                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)

            callApi(Constants.OTHER_VIDEOS,"equipment")
        }

    }
    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
    }


}