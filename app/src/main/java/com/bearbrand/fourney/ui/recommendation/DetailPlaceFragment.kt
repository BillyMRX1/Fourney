package com.bearbrand.fourney.ui.recommendation

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentDetailPlaceBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class DetailPlaceFragment : Fragment() {
    private lateinit var binding: FragmentDetailPlaceBinding
    private val args: DetailPlaceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailPlaceBinding.inflate(inflater, container, false)
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        binding.progress.visibility = View.VISIBLE
        val id = args.id
        val reference = FirebaseFirestore.getInstance().collection("place").document(id)
        reference.get().addOnSuccessListener {
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
            binding.tvPrice.text= it.getString("price")

            val risk = it.getString("risk")
            binding.tvRisk.text = "${risk} Risk"

            if (risk.equals("Low", true)){
                binding.tvRisk.setTextColor(requireContext().getResources().getColor(R.color.green))
                //TO DO: perbaiki drawable tintnya, masih belom bisa
            }else if(risk.equals("Medium", true)){
                binding.tvRisk.setTextColor(requireContext().getResources().getColor(R.color.orange))
            }else if(risk.equals("High", true)){
                binding.tvRisk.setTextColor(requireContext().getResources().getColor(R.color.red))
            }

            val label = it.getBoolean("isOpen")

            if (label == true){
                binding.openLabel.text = "Buka"
            }else{
                binding.openLabel.text =  "Tutup"
                binding.openLabel.background = requireContext().getResources().getDrawable(R.drawable.bg_red)
            }

            binding.tvNumObject.text = "${it.getLong("numObject")} Objek Tersedia"
            binding.tvDesc.text = it.getString("desc")
            binding.tvTime.text = it.getString("time")

        }
    }


}