package com.bearbrand.fourney.ui.reward.tiketku

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ItemKuponBinding
import com.bearbrand.fourney.databinding.ItemKuponkuBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.ui.reward.detail.DetailTicketActivity
import com.bearbrand.fourney.ui.reward.tiketku.detail.DetailMyTicketActivity


class TiketkuAdapter(private val listItem: ArrayList<TiketModel>) :
    RecyclerView.Adapter<TiketkuAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemKuponkuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount() = listItem.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }


    inner class ListViewHolder(private val binding: ItemKuponkuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TiketModel) {
            with(binding) {
                tvValidUntil.text = item.validUntil
                tvVoucherPlace.text = item.voucherPlace
                tvVoucherTitle.text = item.voucherTitle

                when (item.background) {
                    "bg_tiket" -> layoutTiket.setBackgroundResource(R.drawable.bg_tiket)
                    "bg_tiket2" -> layoutTiket.setBackgroundResource(R.drawable.bg_tiket2)
                    "bg_tiket3" -> layoutTiket.setBackgroundResource(R.drawable.bg_tiket3)
                }

            }
            binding.btnDetail.setOnClickListener {
                val intent = Intent(itemView.context, DetailMyTicketActivity::class.java)
                intent.putExtra(DetailMyTicketActivity.TIKETKU, item)
                itemView.context.startActivity(intent)
            }


        }

    }
}