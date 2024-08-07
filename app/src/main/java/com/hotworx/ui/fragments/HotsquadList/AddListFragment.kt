package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatButton
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.hotworx.R
import com.hotworx.databinding.FragmentAddListBinding
import com.hotworx.databinding.FragmentHotsquadCreateBinding
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class AddListFragment : BaseFragment() {
    private var _binding: FragmentAddListBinding? = null
    private val binding get() = _binding!!

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
            val myHotsquadListFragment = MyHotsquadListFragment()
            dockActivity.replaceDockableFragment(myHotsquadListFragment)
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
