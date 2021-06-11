package com.bearbrand.fourney.ui.history.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ItemDetailHistoryBinding
import com.bearbrand.fourney.model.DetailObjectEntity


class DetailHistoryAdapter(private val listItem: ArrayList<DetailObjectEntity>) :
    RecyclerView.Adapter<DetailHistoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemDetailHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    inner class ListViewHolder(private val binding: ItemDetailHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailObjectEntity) {
            with(binding) {
                val coin = "+ ${item.poin} Coin Point"
                if (item.done) {
                    tvObjectName.text = item.location
                    ivChallengeDone.visibility = View.VISIBLE
                    tvObjectName.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.white
                        )
                    )
                    layoutParent.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.blue
                        )
                    )
                    ivLocation.setColorFilter(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.lightbluegreen
                        )
                    )
                    tvPoin.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.lightbluegreen
                        )
                    )
                    tvPoin.text = coin
                } else {
                    tvObjectName.text = item.location
                    tvPoin.text = coin
                }
            }
        }
    }


}