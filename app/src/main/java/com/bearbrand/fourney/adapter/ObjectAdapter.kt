package com.bearbrand.fourney.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bearbrand.fourney.R
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.ListObject
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ObjectAdapter(options: FirestoreRecyclerOptions<ListObject>) : FirestoreRecyclerAdapter<ListObject, ObjectAdapter.ViewHolder>(options) {
    private var listener: OnItemClickListener? = null
    private var mOptions: FirestoreRecyclerOptions<ListObject>? = null
    private var mSnapshot: ObservableSnapshotArray<ListObject>? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var point: TextView? = null
        var title: TextView? = null


        init {
            point = itemView.findViewById(R.id.tv_point)
            title = itemView.findViewById(R.id.tv_name_object)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object_in_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ListObject) {
        holder.title?.text = model.title
        holder.point?.text = "+ ${model.point} Poin"
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