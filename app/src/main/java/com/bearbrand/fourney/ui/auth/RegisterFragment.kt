package com.bearbrand.fourney.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mFragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        mFragmentManager = requireFragmentManager()

        binding.tvMasuk.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.btnBerikutnya.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_masuk -> {
                val mFragmentMasuk = LoginFragment()

                mFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragmentContainerView, mFragmentMasuk, LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.iv_back -> requireActivity().onBackPressed()
            R.id.btn_berikutnya ->{
                val email = binding.tilEmail.editText?.text.toString()
                if (email.isEmpty()){
                    binding.tilEmail.error = "Please fill this field"
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    binding.tilEmail.error = "Invalid email address"
                }
                else{
                    val mFragmentCreate = CreateAccountFragment()

                    val mBundle = Bundle()
                    mBundle.putString(CreateAccountFragment.ARG_EMAIL, binding.etEmail.text.toString())
                    mFragmentCreate.arguments = mBundle

                    mFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.fragmentContainerView, mFragmentCreate, CreateAccountFragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }
            }


        }
    }


}