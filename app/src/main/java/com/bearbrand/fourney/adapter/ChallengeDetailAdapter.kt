package com.bearbrand.fourney.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.ListObject
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.firebase.auth.FirebaseAuth

class ChallengeDetailAdapter(options: FirestoreRecyclerOptions<ListObject>) : FirestoreRecyclerAdapter<ListObject, ChallengeDetailAdapter.ViewHolder>(options) {

    private var listener: OnItemClickListener? = null
    private var mOptions: FirestoreRecyclerOptions<ListObject>? = null
    private var mSnapshot: ObservableSnapshotArray<ListObject>? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var point: TextView? = null
        var title: TextView? = null
        var hint: TextView? = null
        var imgPointer: ImageView
        var background: ConstraintLayout
        var imgScan: ImageView

        init {
            point = itemView.findViewById(R.id.tv_poin)
            title = itemView.findViewById(R.id.tv_name_object)
            hint = itemView.findViewById(R.id.tv_hint)
            imgPointer = itemView.findViewById(R.id.img_pointer)
            background = itemView.findViewById(R.id.background)
            imgScan = itemView.findViewById(R.id.img_scan)

            itemView.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener!!.onItemClick(snapshots.getSnapshot(position), position)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ListObject) {
        if(model.idUser.contains(FirebaseAuth.getInstance().currentUser?.uid)){
            holder.background.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            holder.title?.text = model.title
            holder.point?.text = "+ ${model.coint} Coin"
            holder.hint?.text = model.hint
            holder.title?.setTextColor(Color.WHITE)
            holder.point?.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.lightbluegreen))
            holder.hint?.setTextColor(Color.WHITE)
            holder.imgPointer.setImageResource(R.drawable.ic_location_green)
            holder.imgScan.setImageResource(R.drawable.ic_scan_done)
        }else{
            holder.title?.text = model.title
            holder.point?.text = "+ ${model.coint} Coin"
            holder.hint?.text = model.hint
        }
    }

    override fun updateOptions(options: FirestoreRecyclerOptions<ListObject>) {
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