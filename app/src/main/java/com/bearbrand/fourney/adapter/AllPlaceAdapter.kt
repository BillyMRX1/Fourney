package com.bearbrand.fourney.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bearbrand.fourney.R
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.Place
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AllPlaceAdapter(options: FirestoreRecyclerOptions<Place>, latitude: Double, longitude: Double) : FirestoreRecyclerAdapter<Place, AllPlaceAdapter.ViewHolder>(options) {
    private var listener: OnItemClickListener? = null
    private var mOptions: FirestoreRecyclerOptions<Place>? = null
    private var mSnapshot: ObservableSnapshotArray<Place>? = null

    var lat = latitude
    var long = longitude

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image: ImageView? = null
        var label: TextView? = null
        var title: TextView? = null
        var distance: TextView? = null
        var risk: TextView? = null
        var numObject: TextView? = null
        val btnDetail: FloatingActionButton? = itemView.findViewById(R.id.button_detail)


        init {
            label = itemView.findViewById(R.id.open_label)
            title = itemView.findViewById(R.id.tv_title)
            distance = itemView.findViewById(R.id.tv_distance)
            risk = itemView.findViewById(R.id.tv_risk)
            numObject = itemView.findViewById(R.id.tv_object)
            image = itemView.findViewById(R.id.img_place)
            btnDetail?.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener!!.onItemClick(snapshots.getSnapshot(position), position)
            }
            risk?.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION && listener != null)
                    listener!!.onInfoClick(snapshots.getSnapshot(position), position)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_all_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Place) {
        if (model.isOpen){
            holder.label?.text = "Buka"
        }else{
            holder.label?.text = "Tutup"
            holder.label?.background = holder.itemView.getContext().getResources().getDrawable(R.drawable.bg_red)
        }

        holder.title?.text = model.title
        holder.numObject?.text = "${model.numObject} Objek Ikonik"

        if (model.risk.equals("Low", true)){
            holder.risk?.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green))
            //TO DO: perbaiki drawable tintnya, masih belom bisa
            holder.risk?.compoundDrawables?.get(0)?.setTint(holder.itemView.getContext().getResources().getColor(R.color.green))
        }else if(model.risk.equals("Medium", true)){
            holder.risk?.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.orange))
            holder.risk?.compoundDrawables?.get(0)?.setTint(holder.itemView.getContext().getResources().getColor(R.color.orange))
        }else if(model.risk.equals("High", true)){
            holder.risk?.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red))
            holder.risk?.compoundDrawables?.get(0)?.setTint(holder.itemView.getContext().getResources().getColor(R.color.red))
        }

        holder.risk?.text = "${model.risk} Risk"

        val loc1 = Location("")
        loc1.setLatitude(model.latitude.toDouble())
        loc1.setLongitude(model.longitude.toDouble())

        val loc2 = Location("")
        loc2.setLatitude(lat)
        loc2.setLongitude(long)

        val distanceInMeters: Float = loc1.distanceTo(loc2)/1000

        holder.distance?.text = String.format("%.0f KM", distanceInMeters)



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