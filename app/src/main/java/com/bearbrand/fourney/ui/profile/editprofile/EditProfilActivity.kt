package com.bearbrand.fourney.ui.profile.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityEditProfilBinding
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.profile.ProfileViewModel

class EditProfilActivity : AppCompatActivity() {
    companion object {
        const val USER = "user"
    }

    private lateinit var binding: ActivityEditProfilBinding
    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentValue = intent.getParcelableExtra<UserModel>(USER)
        with(binding) {
            intentValue?.let { item ->
                tilAddress.editText?.setText(item.address)
                tilFullName.editText?.setText(item.name)
                tilPhoneNumber.editText?.setText(item.phone)

                btnBack.setOnClickListener {
                    finish()
                }

                btnEditProfile.setOnClickListener {
                    val newData = UserModel(
                        name = etFullName.text.toString(),
                        uid = item.uid,
                        point = item.point,
                        xp = item.xp,
                        email = item.email,
                        avatar = item.avatar,
                        address = etAddress.text.toString(),
                        phone = etPhoneNumber.text.toString(),
                        location = item.location
                    )
                    viewModel.updateUser(newData)
                    Toast.makeText(this@EditProfilActivity,"Perubahan di Simpan",Toast.LENGTH_LONG).show()
                    finish()
                }
            }


        }


        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}