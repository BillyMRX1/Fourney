package com.bearbrand.fourney.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ItemHistoryBinding
import com.bearbrand.fourney.databinding.ItemLeaderboardBinding
import com.bearbrand.fourney.model.DummyHistoryModel
import com.bearbrand.fourney.model.HistoryModel
import com.bearbrand.fourney.model.LeaderboardModel
import com.bearbrand.fourney.ui.history.detail.DetailHistoryActivity
import com.bumptech.glide.Glide

class HistoryAdapter(private val listItem: ArrayList<DummyHistoryModel>) :
    RecyclerView.Adapter<HistoryAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    inner class ListViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DummyHistoryModel) {
            with(binding) {
                val coin = "+ ${item.coin} CP"
                val xp = "+ ${item.xp} XP"
                tvCoin.text = coin
                tvXp.text = xp
                tvPlace.text = item.idPlace
                tvDate.text = item.date

                val numberChallenge = "${item.challengeDone}/${item.challengeNumber}"
                if (item.challengeDone == item.challengeNumber) {
                    tvNumberChallenge.text = numberChallenge
                    tvNumberChallenge.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.blue
                        )
                    )
                    tvSelesai.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                } else {
                    tvNumberChallenge.text = numberChallenge
                }

                btnNext.setOnClickListener {
                    val intent = Intent(itemView.context,DetailHistoryActivity::class.java)
                    intent.putExtra(DetailHistoryActivity.DETAIL,item)
                    itemView.context.startActivity(intent)
                }
            }


        }

    }
}