package com.bearbrand.fourney.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentHistoryBinding
import com.bearbrand.fourney.databinding.FragmentLeaderboardBinding
import com.bearbrand.fourney.ui.leaderboard.LeaderboardAdapter
import com.bearbrand.fourney.utils.DummyHistory

class HistoryFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            val dummyList = DummyHistory.generateDummyHistory()
            adapter = HistoryAdapter(dummyList)
            rvHistory.adapter = adapter
            adapter.notifyDataSetChanged()
            rvHistory.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

}