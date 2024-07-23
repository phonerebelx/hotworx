package com.hotworx.ui.fragments.DashboardSessionFragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.ui.adapters.DashboardAdapter.DashboardCompletedSessionAdapter
import com.hotworx.ui.adapters.DashboardAdapter.DashboardPendingSessionAdapter
import com.hotworx.ui.fragments.BaseFragment

class CompletedSessionFragment : BaseFragment(), OnItemClickInterface {
    lateinit private var rvCompletedSessions: RecyclerView
    var getTodaysCompletedSession: ArrayList<TodaysPendingSession>? = null
    lateinit private var completedSessionAdapter: DashboardCompletedSessionAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_completed_session, container, false)
        rvCompletedSessions = root.findViewById(R.id.rvCompletedSessions)
        setCompletedSessionAdapter()
        return root
    }

    private fun setCompletedSessionAdapter(){
        val dataSource = getTodaysCompletedSession ?: ArrayList()
        completedSessionAdapter = DashboardCompletedSessionAdapter(requireContext())
        completedSessionAdapter.setList(dataSource)
        rvCompletedSessions.adapter = completedSessionAdapter
    }

    override fun onItemClick(value: String?) {
    }

    fun setData(sessions: ArrayList<TodaysPendingSession>) {
        getTodaysCompletedSession = sessions
    }
}