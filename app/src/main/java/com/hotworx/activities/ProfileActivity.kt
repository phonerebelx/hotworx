package com.hotworx.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.hotworx.R
import com.hotworx.global.Constants
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.ProfileAndGoal.ProfileAndGoalFragment

class ProfileActivity : DockActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupFragment()
    }

    override fun onLoadingStarted() {
    }

    override fun onLoadingFinished() {
    }

    override fun onProgressUpdated(percentLoaded: Int) {
    }

    private fun setupFragment() {
        supportFragmentManager.addOnBackStackChangedListener(getListener())
        replaceDockableFragment(ProfileAndGoalFragment(), Constants.ProfileAndGoalFragment)

    }

    private fun getListener(): FragmentManager.OnBackStackChangedListener {
        return FragmentManager.OnBackStackChangedListener {
            val manager = supportFragmentManager
            if (manager != null) {
                val currFrag = manager.findFragmentById(getDockFrameLayoutId()) as BaseFragment
                currFrag.fragmentResume()
            }
        }
    }

    override fun getDockFrameLayoutId(): Int {
        return R.id.mainFrameLayout
    }
}