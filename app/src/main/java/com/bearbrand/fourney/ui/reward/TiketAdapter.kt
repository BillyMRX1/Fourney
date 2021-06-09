package com.bearbrand.fourney.ui.reward

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.databinding.ItemKuponBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.ui.reward.detail.DetailTicketActivity


class TiketAdapter(private val listItem: ArrayList<TiketModel>) :
    RecyclerView.Adapter<TiketAdapter.ListViewHolder>() {

    var onItemClick: ((TiketModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemKuponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }


    inner class ListViewHolder(private val binding: ItemKuponBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TiketModel) {
            with(binding) {
                tvCoin.text = item.coin.toString()
                tvValidUntil.text = item.validUntil
                tvVoucherPlace.text = item.voucherPlace
                tvVoucherTitle.text = item.voucherTitle
            }
            binding.btnDetail.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context,DetailTicketActivity::class.java))
            }

        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listItem[adapterPosition])
            }
        }
    }
}