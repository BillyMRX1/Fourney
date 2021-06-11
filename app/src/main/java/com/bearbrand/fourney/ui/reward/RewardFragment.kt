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
                CoroutineScope(Dispatchers.IO).launch {
                    val currUser = viewModel.getUser(uid)
                    val userKupon = viewModel.getUserTiket(uid)
                    currUser.let {
                        withContext(Dispatchers.Main){
                            val koin = "${it.point} CP"
                            binding.tvCoin.text = koin
                        }
                    }
                    userKupon.let {
                        val numberChallenge = viewModel.getMyChallenge(uid)
                        withContext(Dispatchers.Main){
                            with(binding){
                                progressBar.visibility = View.GONE
                                allLayout.visibility = View.VISIBLE
                                val tantanganku = "$numberChallenge Tantangan"
                                tvTantanganku.text = tantanganku
                                val kupon = "${userKupon.size} Kupon"
                                tvKupon.text = kupon
                                cardMyCupon.setOnClickListener {
                                    if (userKupon.isNotEmpty()) {
                                        startActivity(
                                            Intent(
                                                requireContext(),
                                                MyTicketActivity::class.java
                                            ).putParcelableArrayListExtra(MyTicketActivity.USER_KUPON, userKupon)
                                        )
                                    } else {
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
