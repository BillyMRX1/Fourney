package com.bearbrand.fourney.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bearbrand.fourney.R
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.Place
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray

class AdvertiseAdapter(options: FirestoreRecyclerOptions<Place>) : FirestoreRecyclerAdapter<Place, AdvertiseAdapter.ViewHolder>(options) {
    private var listener: OnItemClickListener? = null
    private var mOptions: FirestoreRecyclerOptions<Place>? = null
    private var mSnapshot: ObservableSnapshotArray<Place>? = null


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image: ImageView? = null

        init {
            image = itemView.findViewById(R.id.img_advertise)
            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener!!.onItemClick(snapshots.getSnapshot(position), position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advertise_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Place) {
        holder.image?.load(model.image){
            placeholder(R.drawable.no_image)
        }
    }

    override fun updateOptions(options: FirestoreRecyclerOptions<Place>) {
        val listening = mSnapshot?.isListening
        if (mOptions?.owner != null)
            mOptions?.owner?.lifecycle?.removeObserver(this)
        mSnapshot?.clear()
        stopListening()

        mOptions = options
        mSnapshot = options.snapshots
        if (options.owner != null)
            options.owner!!.lifecycle.addObserver(this)
        if (listening!!)
            startListening()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    init {
        mOptions = options
        mSnapshot = options.snapshots
        if (options.owner != null) {
            options.owner!!.lifecycle.addObserver(this)
        }
    }
}