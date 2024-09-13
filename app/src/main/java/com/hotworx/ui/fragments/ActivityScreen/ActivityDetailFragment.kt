package com.hotworx.ui.fragments.ActivityScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.hotworx.R
import com.passio.modulepassio.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.UIHelper
import com.hotworx.helpers.Utils
import com.hotworx.requestEntity.CaloriesDetailPojo
import com.hotworx.requestEntity.FinalSummaryPojo
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.WorkouDetailAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class ActivityDetailFragment : BaseFragment() {
    var unbinder: Unbinder? = null
    var workouDetailAdapter: WorkouDetailAdapter? = null

    @JvmField
    @BindView(R.id.rvDetail)
    var rvDetail: RecyclerView? = null

    @JvmField
    @BindView(R.id.btnFbShare)
    var btnFbShare: Button? = null
    var parentActivityId: String? = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity_detail, container, false)
        unbinder = ButterKnife.bind(this, view)
        val args = arguments
        if (args != null) {
            parentActivityId = args.getString("parent_activity_id")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (parentActivityId != "0") {
            getServiceHelper().enqueueCall(
                getWebService().getFinalSummary(
                    apiHeader(requireContext()),
                    parentActivityId
                ), WebServiceConstants.GET_FULL_SUMMARY, true
            )
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.detail)
    }

    @OnClick(R.id.btnFbShare)
    fun onClick() {
        shareImageOnFacebook()
    }

    private fun shareImageOnFacebook() {
        val bitmap =
            Utils.getScreenshotOfActivity(myDockActivity, Constants.FACEBOOK_REQUEST_COUNTER)
        if (bitmap != null) {
            val filePath = MediaStore.Images.Media.insertImage(
                myDockActivity.contentResolver,
                bitmap,
                "",
                null
            )
            val imageUri = Uri.parse(filePath)

            val shareIntent = Intent()
            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.setType("image/jpg")

            startActivityForResult(
                Intent.createChooser(shareIntent, "Share via"),
                SHARE_IMAGE_REQUEST_CODE
            )
        } else {
            Utils.customToast(myDockActivity, "Failed to take screenshot of the screen")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SHARE_IMAGE_REQUEST_CODE) {
            //Call webservice
            apiCallForShareFacebookActivity(parentActivityId)
        }
    }

    private fun apiCallForShareFacebookActivity(parentActivityId: String?) {
        getServiceHelper().enqueueCall(
            getWebService().shareFacebook(
                apiHeader(requireContext()),
                parentActivityId,
                "ANDROID"
            ), WebServiceConstants.SHARE_FACEBOOK, true
        )
    }

    override fun ResponseSuccess(result: String, Tag: String) {
        when (Tag) {
            WebServiceConstants.GET_FULL_SUMMARY -> {
                val mContentPojo =
                    GsonFactory.getConfiguredGson().fromJson(result, FinalSummaryPojo::class.java)
                if (mContentPojo != null && mContentPojo.allData[0].calorieDetails != null && mContentPojo.allData[0].calorieDetails.forty_burnt != null) updateAdapter(
                    mContentPojo.allData[0].calorieDetails
                )
                else UIHelper.showShortToastInCenter(
                    myDockActivity,
                    getString(R.string.internal_exception_messsage)
                )
            }

            WebServiceConstants.SHARE_FACEBOOK -> {}
        }
    }

    private fun updateAdapter(caloriesDetailPojo: CaloriesDetailPojo) {
        workouDetailAdapter = WorkouDetailAdapter(myDockActivity, caloriesDetailPojo)
        rvDetail!!.layoutManager = LinearLayoutManager(
            myDockActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvDetail!!.adapter = workouDetailAdapter
    }


    override fun onDestroy() {
        super.onDestroy()
        //unbinder.unbind();
    }

    companion object {
        private const val SHARE_IMAGE_REQUEST_CODE = 101
    }
}
