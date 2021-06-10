package com.bearbrand.fourney.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bearbrand.fourney.R
import com.bearbrand.fourney.adapter.ObjectAdapter
import com.bearbrand.fourney.databinding.FragmentChallengeDetailBinding
import com.bearbrand.fourney.model.ListObject
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ChallengeDetailFragment : Fragment() {

    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ChallengeDetailFragmentArgs by navArgs()
    private var adapter: ObjectAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDetail()
        loadObject()
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
        adapter = ObjectAdapter(options)
        binding.rvPlaces.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}