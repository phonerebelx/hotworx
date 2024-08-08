package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.gson.Gson
import com.hotsquad.hotsquadlist.utils.Constant
import com.hotworx.R
import com.hotworx.databinding.FragmentHotsquadCreateBinding
import com.hotworx.databinding.FragmentHotsquadSearchBinding
import com.hotworx.databinding.ItemUserFormBinding
import com.hotworx.models.HotsquadList.SearchUserModel
import com.hotworx.models.HotsquadList.UserFormFieldModel
import com.hotworx.models.HotsquadList.UserModel
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import java.text.Normalizer.Form

class HotsquadSearchFragment : BaseFragment() {
    private var _binding: FragmentHotsquadSearchBinding? = null
    private val binding get() = _binding!!

    /**
     * List
     */
    private val userListForServer = ArrayList<UserModel>()
    private lateinit var uList: SearchUserModel
    private var dynamicField: MutableList<UserFormFieldModel> = ArrayList()

    /**
     * Variables
     */
    var personCount = 0
    /**
     * Dynamic Layout
     */
    private lateinit var bindingItemUserForm: ItemUserFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotsquadSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addUSer.setOnClickListener(View.OnClickListener {

            when {
                TextUtils.isEmpty(binding.titleEt.text.toString()) -> {
                    binding.titleEt.error = "Field Required!!"
                    binding.titleEt.requestFocus()
                }
                else -> {

                    /**
                     * clear before add data
                     */
                    userListForServer.clear()

                    dynamicField.forEach {

                        Log.d(
                            "Dynamic",
                            " User Form Fields \n"
                                .plus("Title: ").plus(it.searchedName.text.toString())
                        )
                        when {
                            TextUtils.isEmpty(it.searchedName.text.toString()) -> {
                                it.searchedName.error = "Field Required!!"
                                it.searchedName.requestFocus()
                            }
                            else -> userListForServer.add(
                                UserModel(
                                    it.searchedName.text.toString()
                                )
                            )
                        }
                    }

                    uList = SearchUserModel(userListForServer)
                }
            }
        })

        bindingItemUserForm = ItemUserFormBinding.inflate(
            layoutInflater,
            binding.layoutForPassengerListForm,
            false
        )
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