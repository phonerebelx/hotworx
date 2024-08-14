package com.hotworx.ui.fragments.HotsquadList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.hotsquad.hotsquadlist.activity.LoginActivity
import com.hotworx.R
import com.hotworx.activities.MainActivity
import com.hotworx.databinding.FragmentHotsquadCreateBinding
import com.hotworx.helpers.Utils
import com.hotworx.helpers.UtilsHelpers
import com.hotworx.interfaces.LoadingListener
import com.hotworx.requestEntity.ExtendedBaseModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateHotsquadFragment : BaseFragment(){
    private var _binding: FragmentHotsquadCreateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotsquadCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.btnCreateHotsquad.setOnClickListener{
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            startActivity(intent)
//        }
        binding.btnCreateHotsquad.setOnClickListener {
            val addListFragment = AddListFragment()
            dockActivity.replaceDockableFragment(addListFragment)
        }
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.leaderboard)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
