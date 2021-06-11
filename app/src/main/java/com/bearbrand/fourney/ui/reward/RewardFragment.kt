package com.bearbrand.fourney.ui.reward

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.databinding.FragmentRewardBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.reward.tiketku.MyTicketActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    override fun onResume() {
        super.onResume()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null){
            viewModel.getListTicket().observe(viewLifecycleOwner, ::setListKupon)
            uid?.let {
                viewModel.getUser(it).observe(viewLifecycleOwner, {
                    with(binding) {
                        val koin = "${it.point} CP"
                        tvCoin.text = koin
                    }
                })
                CoroutineScope(Dispatchers.IO).launch {
                    val userKupon = viewModel.getUserTiket(uid)
                    userKupon.let {
                        val numberChallenge = viewModel.getMyChallenge(uid)
                        withContext(Dispatchers.Main){
                            binding.progressBar.visibility = View.GONE
                            binding.allLayout.visibility = View.VISIBLE
                            val tantanganku = "$numberChallenge Tantangan"
                            binding.tvTantanganku.text = tantanganku
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
