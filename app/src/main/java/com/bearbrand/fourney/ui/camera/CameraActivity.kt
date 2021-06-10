package com.bearbrand.fourney.ui.camera

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bearbrand.fourney.databinding.ActivityCameraBinding
import com.bearbrand.fourney.tflite.Classifier
import com.wonderkiln.camerakit.*
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cameraView.start()
        binding.cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(p0: CameraKitEvent?) {}

            override fun onError(p0: CameraKitError?) {}

            override fun onImage(p0: CameraKitImage) {
                var bitmap = p0.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
                val result = classifier.recognizeImage(bitmap)
                binding.viewSuccess.tvAnswer.text = result[0].title
                Toast.makeText(applicationContext, result[1].title, Toast.LENGTH_SHORT).show()
            }

            override fun onVideo(p0: CameraKitVideo?) {}
        })

        binding.btnShutter.setOnClickListener {
            binding.cameraView.captureImage()
            binding.viewSuccess.root.visibility = View.VISIBLE
            binding.cameraView.stop()
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
    }
}