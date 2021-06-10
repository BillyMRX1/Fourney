package com.bearbrand.fourney.ui.challenge

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentChallengeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!
    private lateinit var reference: CollectionReference
    private lateinit var referenceUser: DocumentReference
    private lateinit var data: DocumentReference
    private val firestore = FirebaseFirestore.getInstance()
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
        loadData()
        loadUser()
    }

    @SuppressLint("SetTextI18n")
    private fun loadUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            data = firestore.collection("users").document(uid)
            data.get().addOnSuccessListener {
                binding.viewChallenge.tvPoin.text = it.getLong("point").toString() + " CP"
                binding.viewChallenge.tvXp.text = it.getLong("xp").toString() + " XP"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        referenceUser = FirebaseFirestore.getInstance().collection("users").document(uid!!)
        referenceUser.get().addOnSuccessListener { document ->
            val locationUser = document.getString("location").toString()
            reference = FirebaseFirestore.getInstance().collection("place")
            val query = reference.whereEqualTo("title", locationUser)
            query.addSnapshotListener { data, _ ->
                if (data != null) {
                    binding.viewChallenge.imgLocation.load(data.documents[0].getString("image")) {
                        placeholder(R.drawable.no_image)
                    }
                    binding.viewChallenge.tvName.text = data.documents[0].getString("title")
                    binding.viewChallenge.tvDescription.text = data.documents[0].getString("desc")
                    binding.viewChallenge.tvRisk.text =
                        data.documents[0].getString("risk") + " Risk"
                    binding.viewChallenge.tvIconicObject.text =
                        "0 / " + data.documents[0].getLong("numObject") + " Objek Ikonik"
                    binding.viewChallenge.root.visibility = View.VISIBLE
                    binding.viewChallenge.btnNext.setOnClickListener {
                        findNavController().navigate(
                            ChallengeFragmentDirections.actionChallengeFragmentToChallengeDetailFragment(
                                data.documents[0].id
                            )
                        )
                    }
                } else {
                    binding.viewError.root.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}