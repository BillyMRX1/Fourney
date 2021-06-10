package com.bearbrand.fourney.ui.reward

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentRewardBinding
import com.bearbrand.fourney.model.TiketModel
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.profile.ProfileViewModel
import com.bearbrand.fourney.ui.reward.tiketku.MyTicketActivity
import com.bearbrand.fourney.utils.DummyTiket
import com.google.firebase.auth.FirebaseAuth


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

        viewModel.getListTicket().observe(viewLifecycleOwner,::setListKupon)
        uid?.let { viewModel.getUser(it).observe(viewLifecycleOwner,::setUser) }
    }

    private fun setUser(user: UserModel) {
        with(binding){
            val tempInt = 0
            val kupon  = "$tempInt Kupon"
            tvKupon.text = kupon
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
            binding.cardMyCupon.setOnClickListener{
                startActivity(Intent(requireContext(),MyTicketActivity::class.java))
            }
        }
    }
}
