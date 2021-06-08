package com.bearbrand.fourney.ui.recommendation

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.adapter.ObjectAdapter
import com.bearbrand.fourney.databinding.FragmentDetailPlaceBinding
import com.bearbrand.fourney.model.ListObject
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*


class DetailPlaceFragment : Fragment() {
    private lateinit var binding: FragmentDetailPlaceBinding
    private val args: DetailPlaceFragmentArgs by navArgs()
    private var adapter: ObjectAdapter? = null
    private lateinit var reference: DocumentReference
    private lateinit var referenceUser: DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.tvRisk.setOnClickListener {
            raiseDialog(args.id)
        }

        loadDataObjects()
//        checkUser()
        return binding.root
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

    private fun checkUser() {
//        reference = FirebaseFirestore.getInstance().collection("place")
//        reference.get().addOnCompleteListener(OnCompleteListener { task ->
//            if (!checkPermissions()) {
//                mPendingGeofenceTask = PendingGeofenceTask.ADD
//                requestPermissions()
//                return@OnCompleteListener
//            }
//            for (document in task.result) {
//                MenuActivity.BAY_AREA_LANDMARKS[document.getString("title")] = LatLng(
//                    java.lang.Double.valueOf(document.getString("latitude")),
//                    java.lang.Double.valueOf(document.getString("longitude"))
//                )
//            }
//
//            //GEOFENCE
//            populateGeofenceList(MenuActivity.BAY_AREA_LANDMARKS)
//            Log.d(MenuActivity.TAG, MenuActivity.BAY_AREA_LANDMARKS.toString())
//            addGeofences()
//            performPendingGeofenceTask()
//        })
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        referenceUser = FirebaseFirestore.getInstance().collection("users").document(uid!!)
        referenceUser.get().addOnSuccessListener { document ->
                reference = FirebaseFirestore.getInstance().collection("place").document(args.id)
                reference.get().addOnSuccessListener { result ->
                    Log.d("cek lokasi", document.getString("location").toString())
                    Log.d("cek title", result.getString("title").toString())
                    if (document.getString("location").toString().equals(result.getString("title").toString())){
                        binding.btnStartChallenge.isEnabled = true
                    }else{
                        binding.btnStartChallenge.isEnabled = false
                    }
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()

    }

    private fun loadDataObjects() {
        val reference = FirebaseFirestore.getInstance().collection("objects").document(args.id)
            .collection("listObjects")
        //val query = reference.whereEqualTo("idPlace", args.id)
        reference.addSnapshotListener { data, _ ->
            if (data != null) {
                if (data.size() > 0) {
                    binding.rvListObject.visibility = View.VISIBLE
                } else {
                    binding.rvListObject.visibility = View.GONE
                }
            }
        }
        val options = FirestoreRecyclerOptions.Builder<ListObject>()
            .setQuery(reference, ListObject::class.java)
            .setLifecycleOwner(activity)
            .build()
        adapter = ObjectAdapter(options)
        binding.rvListObject.adapter = adapter


    }

    private fun loadData() {
        binding.progress.visibility = View.VISIBLE
        val id = args.id
        val reference = FirebaseFirestore.getInstance().collection("place").document(id)
        reference.get().addOnSuccessListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            referenceUser = FirebaseFirestore.getInstance().collection("users").document(uid!!)
            referenceUser.get().addOnSuccessListener { document ->

                val locationUser = document.getString("location").toString()
                val titlePlace = it.getString("title").toString()

                if (locationUser.equals(titlePlace,true)){
                    binding.btnStartChallenge.isEnabled = true
                }else{
                    binding.btnStartChallenge.isEnabled = false
                }
            }
            binding.progress.visibility = View.GONE
            binding.imgPlace.load(it.getString("image"))
            binding.tvTitle.text = it.getString("title")

            val longitude = it.getString("longitude")?.toDouble()
            val latitude = it.getString("latitude")?.toDouble()
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                latitude!!,
                longitude!!,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address: String =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            binding.tvAddress.text = address
            binding.tvPrice.text = it.getString("price")

            val risk = it.getString("risk")
            binding.tvRisk.text = "${risk} Risk"

            if (risk.equals("Low", true)) {
                binding.tvRisk.setTextColor(requireContext().getResources().getColor(R.color.green))
                //TO DO: perbaiki drawable tintnya, masih belom bisa
            } else if (risk.equals("Medium", true)) {
                binding.tvRisk.setTextColor(
                    requireContext().getResources().getColor(R.color.orange)
                )
            } else if (risk.equals("High", true)) {
                binding.tvRisk.setTextColor(requireContext().getResources().getColor(R.color.red))
            }

            val label = it.getBoolean("isOpen")

            if (label == true) {
                binding.openLabel.text = "Buka"
            } else {
                binding.openLabel.text = "Tutup"
                binding.openLabel.background =
                    requireContext().getResources().getDrawable(R.drawable.bg_red)
            }

            binding.tvNumObject.text = "${it.getLong("numObject")} Objek Tersedia"
            binding.tvDesc.text = it.getString("desc")
            binding.tvTime.text = it.getString("time")

        }
    }


}