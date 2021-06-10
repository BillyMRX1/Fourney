package com.bearbrand.fourney.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.fragment.app.activityViewModels
import com.bearbrand.fourney.R
import com.bearbrand.fourney.activity.AuthActivity
import com.bearbrand.fourney.databinding.FragmentProfileBinding
import com.bearbrand.fourney.model.UserModel
import com.bearbrand.fourney.ui.profile.editprofile.EditProfilActivity
import com.bearbrand.fourney.ui.profile.resetpassword.ResetPasswordActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by activityViewModels()
    private val uid = firebaseAuth.currentUser?.uid
    var photoMax: Int = 1
    var photoLocation: Uri? = null
    lateinit var storageRef: StorageReference
    var profilePic: String = ""


    //Get Image
    private fun getFileExtension(uri: Uri?): String? {
        val contentResolver = activity?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver?.getType(uri!!))
    }

    private fun findPhoto() {
        val pic = Intent()
        pic.type = "image/*"
        pic.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(pic, photoMax)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == photoMax && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            photoLocation = data.data
            Glide.with(requireContext())
                .load(photoLocation)
                .into(binding.imgProfile)
            uploadImage()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid?.let { viewModel.getUser(it).observe(viewLifecycleOwner, ::setProfile) }
    }

    @SuppressLint("SetTextI18n")
    private fun setProfile(user: UserModel) {
        with(binding) {
            user.let {
                val imageAvatar =
                    if (it.avatar.isNotEmpty()) it.avatar else R.drawable.ic_base_avatar
                Glide.with(requireActivity())
                    .load(imageAvatar)
                    .placeholder(R.drawable.no_image)
                    .into(imgProfile)

                tvName.text = it.name
                tvCoin.text = it.point.toString() + " CP"
                tvXp.text = it.xp.toString() + " XP"

                btnUbahFoto.setOnClickListener {
                    findPhoto()
                    uploadImage()
                }
                btnEditProfile.setOnClickListener {
                    val intent = Intent(requireContext(),EditProfilActivity::class.java)
                    intent.putExtra(EditProfilActivity.USER,user)
                    startActivity(intent)
                }
                btnUbahPassword.setOnClickListener {
                    val intent = Intent(requireContext(),ResetPasswordActivity::class.java)
                    intent.putExtra(ResetPasswordActivity.USER,user.email)
                    startActivity(intent)
                }

                btnLogout.setOnClickListener {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Log out")
                    dialog.setMessage("Apakah kamu yakin ingin keluar?")
                    dialog.setPositiveButton("Iya") { dialog: DialogInterface?, which: Int ->
                        firebaseAuth.signOut()
                        startActivity(Intent(requireContext(), AuthActivity::class.java))

                    }
                    dialog.setNegativeButton("Tidak") { dialog: DialogInterface?, which: Int -> }
                    dialog.show()
                }


            }
        }

    }

    private fun uploadImage() {
        if (photoLocation != null) {
            storageRef = FirebaseStorage.getInstance().reference.child("AvatarPath")
            val storage = storageRef.child(
                System.currentTimeMillis()
                    .toString() + "." + getFileExtension(photoLocation)
            )
            photoLocation?.let { it1 ->
                storage.putFile(it1).addOnSuccessListener {
                    storage.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        profilePic = uri.toString()
                        uid?.let { id -> viewModel.changeProfileImage(id,  profilePic) }

                    })
                }
            }
        }
    }
}