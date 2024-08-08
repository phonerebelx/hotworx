package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.databinding.FragmentHotsquadSearchBinding
import com.hotworx.models.HotsquadList.UserModel
import com.hotworx.ui.adapters.HotsquadListAdapter.UserListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import java.util.regex.Pattern

class HotsquadSearchFragment : BaseFragment() {
    private var _binding: FragmentHotsquadSearchBinding? = null
    private val binding get() = _binding!!

    private val userList = mutableListOf<UserModel>()
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotsquadSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userListAdapter = UserListAdapter(userList)
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
            }
        }
    }

    private fun isValidEmailOrPhone(input: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        val phonePattern = Pattern.compile("^\\+?[0-9]{10,13}\$")
        return emailPattern.matcher(input).matches() || phonePattern.matcher(input).matches()
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
