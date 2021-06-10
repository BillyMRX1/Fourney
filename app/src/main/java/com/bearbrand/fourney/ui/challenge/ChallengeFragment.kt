package com.bearbrand.fourney.ui.challenge

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bearbrand.fourney.databinding.FragmentChallengeBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.*


class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private lateinit var referenceUser: DocumentReference
    private lateinit var reference: DocumentReference
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChallengeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val uid = FirebaseAuth.getInstance().currentUser?.uid
//        referenceUser = FirebaseFirestore.getInstance().collection("users").document(uid!!)
//        referenceUser.get().addOnSuccessListener { document ->
//            reference = FirebaseFirestore.getInstance().collection("place").document()
//            reference.get().addOnSuccessListener { result ->
//                Log.d("cek lokasi", document.getString("location").toString())
//                Log.d("cek title", result.getString("title").toString())
//                if (document.getString("location").toString().equals(result.getString("title").toString())){
//                    binding.btnStartChallenge.isEnabled = true
//                }else{
//                    binding.btnStartChallenge.isEnabled = false
//                }
//            }
//
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val PLACE_ID = "place_id"
    }
}