package com.bearbrand.fourney.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bearbrand.fourney.MenuActivity
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private var checkUser: Boolean = false
    lateinit var mFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        mFragmentManager = requireFragmentManager()
        auth = FirebaseAuth.getInstance()

        binding.tvDaftar.setOnClickListener (this)
        binding.btnMasuk.setOnClickListener(this)
        binding.ivBack.setOnClickListener (this)

        return binding.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_daftar -> {
                val mFragmentDaftar = RegisterFragment()

                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragmentContainerView, mFragmentDaftar, RegisterFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.iv_back -> requireActivity().onBackPressed()
            R.id.btn_masuk ->{
                binding.progress.visibility = View.VISIBLE
                val email = binding.tilEmail.editText?.text.toString()
                val pass = binding.tilPassword.editText?.text.toString()

                if (!isValidLogin(email, pass)) binding.progress.visibility = View.GONE
                else loginUser(email, pass)
            }
        }
    }

    private fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val reference = FirebaseFirestore.getInstance().collection("users")
                reference.get().addOnSuccessListener { result ->
                    for (document in result) {
                        if (auth.uid.equals(document.id)) {
                            checkUser = true
                        }
                    }
                    if (checkUser) {
                        startActivity(Intent(context, MenuActivity::class.java))
                        requireActivity().finish()
                    } else {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Please use another app to access psycholog features",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            binding.progress.visibility = View.GONE
        }

    }

    private fun isValidLogin(email: String, pass: String): Boolean {
        val empty = "Please fill this field"
        val invalid = "Invalid Email Address"

        return when {
            email.isEmpty() -> {
                binding.tilEmail.error = empty
                false
            }
            pass.isEmpty() -> {
                binding.tilPassword.error = empty
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = invalid
                false
            }
            else -> {
                binding.tilEmail.error = null
                binding.tilPassword.error = null
                binding.tilPassword.isErrorEnabled
                binding.tilEmail.isErrorEnabled
                true
            }
        }
    }
}