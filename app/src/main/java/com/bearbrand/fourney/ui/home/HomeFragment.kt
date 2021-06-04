package com.bearbrand.fourney.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bearbrand.fourney.R
import com.bearbrand.fourney.activity.AuthActivity
import com.bearbrand.fourney.adapter.AdvertiseAdapter
import com.bearbrand.fourney.databinding.FragmentHomeBinding
import com.bearbrand.fourney.helper.OnItemClickListener
import com.bearbrand.fourney.model.Place
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: AdvertiseAdapter? = null
    private lateinit var reference: CollectionReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //init color text
        var name = "Pengunjung"
        val styledText = "Halo, <font color='#2D74FF'>$name</font>"
        binding.tvHelloName.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        reference = FirebaseFirestore.getInstance().collection("place")
        val query = reference.whereEqualTo("isAdvertised", true)
        query.addSnapshotListener { data, _ ->
            if (data != null) {
                if (data.size() > 0) {
                    binding.rvAdvertiseBanner.visibility = View.VISIBLE
                } else {
                    binding.rvAdvertiseBanner.visibility = View.GONE
                }
            }
        }
        val options = FirestoreRecyclerOptions.Builder<Place>()
            .setQuery(query, Place::class.java)
            .setLifecycleOwner(activity)
            .build()
        adapter = AdvertiseAdapter(options)
        binding.rvAdvertiseBanner.adapter = adapter

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(snapshot: DocumentSnapshot, position: Int) {

            }
        })


    }

}