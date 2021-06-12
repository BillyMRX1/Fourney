package com.bearbrand.fourney.ui.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bearbrand.fourney.databinding.ActivityCameraBinding
import com.bearbrand.fourney.tflite.Classifier
import com.bearbrand.fourney.tflite.IClassifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.wonderkiln.camerakit.*
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var objectId: String
    private lateinit var locationId: String
    private lateinit var result: List<IClassifier.Recognition>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        objectId = intent.getStringExtra(OBJECT_ID).toString()
        locationId = intent.getStringExtra(LOCATION_ID).toString()
        Log.d("OBJECT: ", objectId)
        binding.cameraView.start()
        binding.cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(p0: CameraKitEvent?) {}

            override fun onError(p0: CameraKitError?) {}

            override fun onImage(p0: CameraKitImage) {
                var bitmap = p0.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
                result = classifier.recognizeImage(bitmap)
                Log.d("SCAN: ", result.toString())
                if (result[0].confidence >= 0.9) {
                    Log.d("ID", "title " + result[0].title)
                    Log.d("ID", "objectId $objectId")
                    if (result[0].title == objectId) {
                        getData()
                        binding.viewSuccess.root.visibility = View.VISIBLE
                    } else {
                        binding.viewFailed.root.visibility = View.VISIBLE
                        Log.d("Failed: ", "Failed 1")
                    }
                } else {
                    binding.viewFailed.root.visibility = View.VISIBLE
                    Log.d("Failed: ", "Failed 3")
                }
            }

            override fun onVideo(p0: CameraKitVideo?) {}
        })

        binding.btnShutter.setOnClickListener {
            binding.cameraView.captureImage()
            binding.cameraView.stop()
        }

        binding.viewSuccess.btnNext.setOnClickListener {
            val reference =
                FirebaseFirestore.getInstance().collection("objects").document(locationId)
                    .collection("listObjects").document(objectId)
            val user = FirebaseAuth.getInstance().currentUser
            val referenceUser =
                FirebaseFirestore.getInstance().collection("users").document(user!!.uid)
            referenceUser.get().addOnSuccessListener {
                var poin = it.getLong("point")
                var xp = it.getLong("xp")
                reference.addSnapshotListener { data, _ ->
                    poin = poin?.plus(data!!.getLong("coint")!!)
                    xp = xp?.plus(data!!.getLong("xp")!!)
                    referenceUser.update("point", poin)
                    referenceUser.update("xp", xp)
                }
            }
            reference.update("idUser", FieldValue.arrayUnion(user.uid))
            onBackPressed()
        }
        initTensorFlowAndLoadModel()
    }

    override fun onPause() {
        binding.cameraView.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier.close() }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val reference = FirebaseFirestore.getInstance().collection("objects").document(locationId)
            .collection("listObjects").document(objectId)
        reference.addSnapshotListener { data, _ ->
            if (data != null) {
                binding.apply {
                    viewSuccess.tvCoin.text = "+ " + data.getLong("coint").toString() + " CP"
                    viewSuccess.tvXp.text = "+ " + data.getLong("xp").toString() + " XP"
                    viewSuccess.tvAnswer.text = data.getString("desc")
                }
            }
        }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = Classifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE
                )
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    companion object {
        private const val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
        const val OBJECT_ID = "object_id"
        const val LOCATION_ID = "location_id"
    }
}