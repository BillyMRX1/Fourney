package com.bearbrand.fourney.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentCreateAccountBinding
import com.bearbrand.fourney.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateAccountFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentCreateAccountBinding
    private lateinit var auth: FirebaseAuth
    var email: String = ""

    companion object {
        var ARG_EMAIL = "email"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        binding.btnBuatAkun.setOnClickListener(this)
        binding.etFullName.setOnClickListener {
            binding.tilFullName.error = null
            binding.tilFullName.isErrorEnabled
        }
        binding.etAddress.setOnClickListener {
            binding.tilAddress.error = null
            binding.tilAddress.isErrorEnabled
        }
        binding.etPhoneNumber.setOnClickListener {
            binding.tilPhoneNumber.error = null
            binding.tilPhoneNumber.isErrorEnabled
        }
        binding.etPassword.setOnClickListener {
            binding.tilPassword.error = null
            binding.tilPassword.isErrorEnabled
        }
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            val emailArg = arguments?.getString(ARG_EMAIL)
            email = emailArg!!
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> requireActivity().onBackPressed()
            R.id.btn_buat_akun -> {
                binding.progress.visibility = View.VISIBLE
                val name = binding.tilFullName.editText?.text.toString()
                val pass = binding.tilPassword.editText?.text.toString()
                val phone = binding.tilPhoneNumber.editText?.text.toString()
                val address = binding.tilAddress.editText?.text.toString()
                val location = ""

                if (!isValidRegister(name, pass, phone, address))
                    binding.progress.visibility = View.GONE
                else
                    registerUser(name, pass, phone, address, location)
            }
        }
    }

    private fun registerUser(name: String, pass: String, phone: String, address: String, location: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                saveUserData(user?.uid!!, email, name, phone, address, location)
            } else {
                Toast.makeText(context, "Failed to Register", Toast.LENGTH_SHORT).show()
            }
            binding.progress.visibility = View.GONE
        }.addOnFailureListener {
            binding.progress.visibility = View.GONE
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveUserData(
        uid: String,
        email: String,
        name: String,
        phone: String,
        address: String,
        location: String
    ) {
        val data = FirebaseFirestore.getInstance().collection("users").document(uid)
        val userData = UserModel(
            name,
            uid,
            0,
            0,
            email,
            "",
            address,
            location,
            phone
        )
        data.set(userData).addOnCompleteListener {
            Toast.makeText(context, "Register Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, MenuActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun isValidRegister(
        name: String,
        pass: String,
        phone: String,
        address: String
    ): Boolean {
        val empty = "Please fill this field"
        val invalid = "Invalid Phone Number"

        return when {
            name.isEmpty() -> {
                binding.tilFullName.error = empty
                false
            }
            pass.isEmpty() -> {
                binding.tilPassword.error = empty
                false
            }
            phone.isEmpty() -> {
                binding.tilPhoneNumber.error = empty
                false
            }
            address.isEmpty() -> {
                binding.tilAddress.error = empty
                false
            }
            !Patterns.PHONE.matcher(phone).matches() -> {
                binding.tilPhoneNumber.error = invalid
                false
            }
            pass.length < 6 -> {
                binding.tilPassword.error = "Password must be at least 6 character"
                false
            }

            else -> {
                binding.tilFullName.error = null
                binding.tilPassword.error = null
                binding.tilPhoneNumber.error = null
                binding.tilAddress.error = null

                binding.tilFullName.isErrorEnabled
                binding.tilPassword.isErrorEnabled
                binding.tilPhoneNumber.isErrorEnabled
                binding.tilAddress.isErrorEnabled
                true
            }
        }

    }

}