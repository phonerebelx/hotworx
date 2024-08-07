package com.hotworx.ui.fragments.HotsquadList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotworx.R
import com.hotworx.databinding.FragmentMyHotsquadListBinding
import com.hotworx.models.HotsquadList.HotsquadItem
import com.hotworx.ui.adapters.HotsquadListAdapter.SquadListAdapter
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar

class MyHotsquadListFragment : BaseFragment(), SquadListAdapter.OnItemClickListener {
    private var _binding: FragmentMyHotsquadListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyHotsquadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setAdapter()
    }

    override fun onItemClick(item: HotsquadItem) {
        // Handle item click here
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.leaderboard)
    }

    private fun setAdapter() {

        val squadList = listOf(
            HotsquadItem("My Family", 2, R.drawable.listicon),
            HotsquadItem("My Friends & Family", 12, R.drawable.listicon),
            HotsquadItem("Hot ISO", 0, R.drawable.listicon)
        )

        val adapter = SquadListAdapter(squadList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
