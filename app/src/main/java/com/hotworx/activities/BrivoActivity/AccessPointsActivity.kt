package com.hotworx.activities.BrivoActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.brivo.sdk.onair.model.BrivoAccessPoint
import com.brivo.sdk.onair.model.BrivoSite
import com.demo.sample.kotlin.BrivoSampleConstants
//import com.demo.sample.kotlin.BrivoSampleConstants
//import com.demo.sample.kotlin.R
//import com.demo.sample.kotlin.databinding.ActivityAccessPointsBinding
//import com.demo.sample.kotlin.ui.adapters.AccessPointsAdapter
//import com.demo.sample.kotlin.ui.adapters.AccessPointsAdapter.AdapterItemClickListener
import com.google.gson.Gson
import com.hotworx.R
import com.hotworx.databinding.ActivityAccessPointsBinding
import com.hotworx.ui.adapters.BrivoAdapter.AccessPointsAdapter

class AccessPointsActivity : AppCompatActivity() {

    private lateinit var brivoAccessPoints: ArrayList<BrivoAccessPoint>
    private lateinit var passId: String
    private lateinit var rvSiteList: RecyclerView
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAccessPointsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_access_points)
        toolbar = binding.toolbar
        initializeUI()
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        rvSiteList = binding.rvSiteList
        val intent = intent
        val message = intent.getStringExtra(BrivoSampleConstants.SELECTED_SITE)
        passId = intent.getStringExtra(BrivoSampleConstants.PASS_ID) ?: ""

        populateAccessPointsList(Gson().fromJson(message, BrivoSite::class.java).accessPoints)
    }


    private fun initializeUI() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

        private fun populateAccessPointsList(brivoAccessPoints: ArrayList<BrivoAccessPoint>) {
        this.brivoAccessPoints = brivoAccessPoints
        val sitesAdapter =
            AccessPointsAdapter(this, passId, brivoAccessPoints, object :
                AccessPointsAdapter.AdapterItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    this@AccessPointsActivity.onAccessPointClicked(view, position)
                }
            })
        rvSiteList.adapter = sitesAdapter
    }

    private fun onAccessPointClicked(view: View, position: Int) {
        val item = brivoAccessPoints[position]
        val selectedAccessPoint = Gson().toJson(item)
        val context = view.context
        val intent = Intent(context, UnlockAccessPointActivity::class.java)
        intent.putExtra(BrivoSampleConstants.SELECTED_ACCESS_POINT, selectedAccessPoint)
        intent.putExtra(BrivoSampleConstants.PASS_ID, passId)
        context.startActivity(intent)
    }

}
