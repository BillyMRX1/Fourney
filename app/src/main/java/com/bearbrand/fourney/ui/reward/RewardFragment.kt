package com.bearbrand.fourney.ui.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentRewardBinding
import com.bearbrand.fourney.utils.DummyTiket


class RewardFragment : Fragment() {

    private lateinit var adapter: TiketAdapter
    private lateinit var binding: FragmentRewardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val listItem = DummyTiket.getTiket()
            adapter = TiketAdapter(listItem)
            adapter.onItemClick = { selectedData ->

            }

            rvTiket.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.rvTiket.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }
}
