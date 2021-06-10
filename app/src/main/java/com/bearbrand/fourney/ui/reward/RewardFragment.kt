package com.bearbrand.fourney.ui.reward

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentRewardBinding
import com.bearbrand.fourney.model.HistoryModel
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.model.UserKuponModel
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.profile.ProfileViewModel
import com.bearbrand.fourney.ui.reward.tiketku.MyTicketActivity
import com.bearbrand.fourney.utils.DummyTiket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RewardFragment : Fragment() {

    private lateinit var adapter: TiketAdapter
    private lateinit var binding: FragmentRewardBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val viewModel: RewardViewModel by activityViewModels()
    private val uid = firebaseAuth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getListTicket().observe(viewLifecycleOwner, ::setListKupon)
        uid?.let {
            viewModel.getUser(it).observe(viewLifecycleOwner, ::setUser)
            CoroutineScope(Dispatchers.IO).launch {
                val userKupon = viewModel.getUserTiket(uid)
                userKupon.let {

                    withContext(Dispatchers.Main){
                        binding.progressBar.visibility = View.GONE
                        binding.allLayout.visibility = View.VISIBLE

                        val kupon = "${userKupon.size} Kupon"
                        binding.tvKupon.text = kupon
                        binding.cardMyCupon.setOnClickListener {
                            if (userKupon.isNotEmpty()) {
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        MyTicketActivity::class.java
                                    ).putParcelableArrayListExtra(MyTicketActivity.USER_KUPON, userKupon)
                                )
                            } else {
                                Toast.makeText(requireContext(), "TES MASUKK", Toast.LENGTH_SHORT).show()
                                startActivity(
                                    Intent(
                                        requireContext(),
                                        MyTicketActivity::class.java
                                    )
                                )
                            }
                        }

                    }

                }
            }

        }
    }


    private fun setUser(user: UserModel) {
        with(binding) {
            val tempInt = 0
            val koin = "${user.point} CP"
            tvCoin.text = koin
            val tantanganku = "$tempInt Tantangan"
            tvTantanganku.text = tantanganku
        }
    }

    private fun setListKupon(listItem: ArrayList<TiketModel>) {
        with(binding) {
            adapter = TiketAdapter(listItem)
            rvTiket.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.rvTiket.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }


}
