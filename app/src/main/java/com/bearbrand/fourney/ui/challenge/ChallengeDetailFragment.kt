package com.bearbrand.fourney.ui.challenge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bearbrand.fourney.adapter.ChallengeDetailAdapter
import com.bearbrand.fourney.databinding.FragmentChallengeDetailBinding
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.ListObject
import com.bearbrand.fourney.ui.camera.CameraActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ChallengeDetailFragment : Fragment() {

    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private var adapter: ChallengeDetailAdapter? = null
    private lateinit var data: DocumentReference
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChallengeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDetail()
        loadObject()
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    @SuppressLint("SetTextI18n")
    private fun loadUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            data = firestore.collection("users").document(uid)
            data.addSnapshotListener { data, _ ->
                binding.tvPoin.text = data?.getLong("point").toString() + " CP"
                binding.tvXp.text = data?.getLong("xp").toString() + " XP"
            }
        }
    }

    private fun loadDetail() {
        val id = args.id
        val reference = FirebaseFirestore.getInstance().collection("place").document(id)
        reference.get().addOnSuccessListener {
            binding.tvLocationName.text = it.getString("title")
        }
    }

    private fun loadObject() {
        val reference = FirebaseFirestore.getInstance().collection("objects").document(args.id)
            .collection("listObjects")
        reference.addSnapshotListener { data, _ ->
            if (data != null) {
                if (data.size() > 0) {
                    binding.rvPlaces.visibility = View.VISIBLE
                } else {
                    binding.rvPlaces.visibility = View.GONE
                }
            }
        }
        val options = FirestoreRecyclerOptions.Builder<ListObject>()
            .setQuery(reference, ListObject::class.java)
            .setLifecycleOwner(activity)
            .build()
        adapter = ChallengeDetailAdapter(options)
        binding.rvPlaces.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(snapshot: DocumentSnapshot, position: Int) {
                val id = snapshot.id
                val intent = Intent(requireContext(), CameraActivity::class.java)
                intent.putExtra(CameraActivity.OBJECT_ID, id)
                intent.putExtra(CameraActivity.LOCATION_ID, args.id)
                startActivity(intent)
            }

            override fun onInfoClick(snapshot: DocumentSnapshot, position: Int) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}