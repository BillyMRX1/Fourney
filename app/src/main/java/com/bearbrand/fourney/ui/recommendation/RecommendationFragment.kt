package com.bearbrand.fourney.ui.recommendation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bearbrand.fourney.R
import com.bearbrand.fourney.adapter.RecommendationAdapter
import com.bearbrand.fourney.databinding.FragmentRecommendationBinding
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.Place
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.*

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var adapter: RecommendationAdapter? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private lateinit var reference: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendationBinding.inflate(inflater, container, false)

        accessLocation()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        reference = FirebaseFirestore.getInstance().collection("place")
        val query = reference.whereEqualTo("risk", "Low")
        query.addSnapshotListener { data, _ ->
            if (data != null) {
                if (data.size() > 0) {
                    binding.rvRecommendation.visibility = View.VISIBLE
                } else {
                    binding.rvRecommendation.visibility = View.GONE
                }
            }
        }
        val options = FirestoreRecyclerOptions.Builder<Place>()
            .setQuery(query, Place::class.java)
            .setLifecycleOwner(activity)
            .build()
        adapter = RecommendationAdapter(options, latitude, longitude)
        binding.rvRecommendation.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(snapshot: DocumentSnapshot, position: Int) {
                val id = snapshot.id
                findNavController().navigate(TouristAttractionFragmentDirections.actionTouristAttractionFragmentToDetailPlaceFragment2(id,position))
            }

            override fun onInfoClick(snapshot: DocumentSnapshot, position: Int) {
                raiseDialog(snapshot.id)
            }
        })


    }

    private fun raiseDialog(id: String) {
        val materialBuilder = MaterialAlertDialogBuilder(requireContext()).create()
        val inflater = layoutInflater.inflate(R.layout.dialog_info_risk, null)
        val btnOk: MaterialButton = inflater.findViewById(R.id.btn_ok)
        val risk: TextView = inflater.findViewById(R.id.tv_risk)
        val numPeople: TextView = inflater.findViewById(R.id.tv_num_people)
        val close: ImageView = inflater.findViewById(R.id.iv_close)

        val reference = FirebaseFirestore.getInstance().collection("place").document(id)
        reference.get().addOnSuccessListener {
            val referenceUser = FirebaseFirestore.getInstance().collection("users")
            val query = referenceUser.whereEqualTo("location", it.getString("title"))
            query.addSnapshotListener { data, _ ->
                if (data != null) {
                    if (data.size() > 0) {
                        numPeople.text = "${data.size()} Orang"
                        var getRisk= ""
                        if (data.size() <= 50)  getRisk = "Low"
                        if (data.size() > 50 && data.size() <= 100 ) getRisk = "Medium"
                        if (data.size() > 100) getRisk = "High"

                        risk.text = "${getRisk} Risk"

                        if (getRisk.equals("Low", true)){
                            getContext()?.getResources()?.let { it1 -> risk?.setTextColor(it1.getColor(R.color.green)) }
                            getContext()?.getResources()?.let { it1 -> numPeople?.setTextColor(it1.getColor(R.color.green)) }

                        }else if(getRisk.equals("Medium", true)){
                            getContext()?.getResources()?.let { it1 -> risk?.setTextColor(it1.getColor(R.color.orange)) }
                            getContext()?.getResources()?.let { it1 -> numPeople?.setTextColor(it1.getColor(R.color.orange)) }

                        }else if(getRisk.equals("High", true)){
                            getContext()?.getResources()?.let { it1 -> risk?.setTextColor(it1.getColor(R.color.red)) }
                            getContext()?.getResources()?.let { it1 -> numPeople?.setTextColor(it1.getColor(R.color.red)) }
                        }

                    }
                }
            }
        }
        btnOk.setOnClickListener {
            materialBuilder.dismiss()
        }
        close.setOnClickListener {
            materialBuilder.dismiss()
        }
        materialBuilder.setView(inflater)
        materialBuilder.show()
    }

    private fun accessLocation() {
        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.getLastLocation()?.addOnCompleteListener(OnCompleteListener<Location?> { task ->
                    val location = task.result
                    if (location != null) {
                        try {
                            val geocoder =
                                Geocoder(context, Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            longitude = addresses[0].longitude
                            latitude = addresses[0].latitude
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(context, "Location null", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            fusedLocationClient?.getLastLocation()?.addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
    }


}