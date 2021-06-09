package com.bearbrand.fourney.ui.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentHomeBinding
import com.bearbrand.fourney.databinding.FragmentLeaderboardBinding
import com.bearbrand.fourney.model.LeaderboardModel
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.profile.ProfileViewModel
import com.bearbrand.fourney.ui.reward.tiketku.TiketkuAdapter
import com.bearbrand.fourney.utils.DummyLeaderBoard
import com.bearbrand.fourney.utils.DummyTiket
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


class LeaderboardFragment : Fragment() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val uid = firebaseAuth.currentUser?.uid
    private val viewModel: LeaderboardViewModel by activityViewModels()
    private lateinit var adapter: LeaderboardAdapter
    private lateinit var binding: FragmentLeaderboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (uid != null) {
            viewModel.getLeaderboard(uid)
                .first.observe(viewLifecycleOwner, ::setListLeaderboard)
            viewModel.getLeaderboard(uid)
                .second.observe(viewLifecycleOwner, ::setUser)
        }
    }

    private fun setUser(user: LeaderboardModel) {
        with(binding) {
            tvXp.text = user.xp.toString()
            tvMyPosition.text = user.rank.toString()
            tvUsername.text = user.username
            val imageAvatar =
                if (user.imgPhoto.isNotEmpty()) user.imgPhoto else R.drawable.ic_base_avatar

            Glide.with(requireContext())
                .load(imageAvatar)
                .placeholder(R.drawable.no_image)
                .into(imgProfile)
        }
    }


    private fun setListLeaderboard(listItem: ArrayList<LeaderboardModel>) {
        with(binding) {
            adapter = LeaderboardAdapter(listItem)
            rvLeaderboard.adapter = adapter
            adapter.notifyDataSetChanged()
            rvLeaderboard.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

}