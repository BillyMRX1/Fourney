package com.bearbrand.fourney.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentHistoryBinding
import com.bearbrand.fourney.databinding.FragmentLeaderboardBinding
import com.bearbrand.fourney.ui.leaderboard.LeaderboardAdapter
import com.bearbrand.fourney.ui.reward.RewardViewModel
import com.bearbrand.fourney.utils.DummyHistory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by activityViewModels()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            uid?.let { uid ->
                CoroutineScope(Dispatchers.IO).launch {
                    val listHistory = viewModel.getHistory(uid)
                    withContext(Dispatchers.Main) {
                        if(listHistory.isEmpty()){
                            historyEmpty.visibility = View.VISIBLE
                            progressBar.visibility  = View.GONE
                        }else{
                            progressBar.visibility  = View.GONE
                            rvHistory.visibility = View.VISIBLE
                            adapter = HistoryAdapter(listHistory)
                            rvHistory.adapter = adapter
                            adapter.notifyDataSetChanged()
                            rvHistory.layoutManager = LinearLayoutManager(
                                activity,
                                LinearLayoutManager.VERTICAL, false
                            )
                        }
                    }
                }

            }
        }
    }

}