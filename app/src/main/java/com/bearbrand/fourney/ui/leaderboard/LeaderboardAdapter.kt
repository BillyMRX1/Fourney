package com.bearbrand.fourney.ui.leaderboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ItemLeaderboardBinding
import com.bearbrand.fourney.model.LeaderboardModel
import com.bumptech.glide.Glide


class LeaderboardAdapter(private val listItem: ArrayList<LeaderboardModel>) :
    RecyclerView.Adapter<LeaderboardAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return if (listItem.size >= 10) {
            12
        } else {
            listItem.size
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    inner class ListViewHolder(private val binding: ItemLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LeaderboardModel) {
            with(binding) {
                tvName.text = item.username
                if (item.rank < 4) {
                    tvRankImage.visibility = View.VISIBLE
                    tvRank.visibility = View.GONE
                    when (item.rank) {
                        1 -> tvRankImage.setBackgroundResource(R.drawable.ic_rank_1)
                        2 -> tvRankImage.setBackgroundResource(R.drawable.ic_rank_2)
                        3 -> tvRankImage.setBackgroundResource(R.drawable.ic_rank_3)
                    }
                } else {
                    tvRankImage.visibility = View.GONE
                    tvRank.visibility = View.VISIBLE
                    tvRank.text = item.rank.toString()
                }
                tvXp.text ="${item.xp} XP"
                val imageAvatar =
                    if (item.imgPhoto.isNotEmpty()) item.imgPhoto else R.drawable.ic_base_avatar
                Glide.with(itemView.context)
                    .load(imageAvatar)
                    .placeholder(R.drawable.no_image)
                    .into(imgProfile)
            }


        }

    }
}