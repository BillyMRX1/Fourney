package com.bearbrand.fourney.ui.profile.resetpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.bearbrand.fourney.R
import com.bearbrand.fourney.databinding.ActivityEditProfilBinding
import com.bearbrand.fourney.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    companion object {
        const val USER = "user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            val intentValue = intent.getStringExtra(USER)
            tilEmail.editText?.setText(intentValue)

            btnBack.setOnClickListener {
                finish()
            }
            btnReset.setOnClickListener {
                val email = etEmail.text.toString().trim()

                if(email.isEmpty()){
                    etEmail.error = "Email tidak boleh kosong"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.error = "Email tidak valid"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this@ResetPasswordActivity, "Cek email untuk reset password", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}