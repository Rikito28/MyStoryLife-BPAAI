package com.riski.mystorylife.ui.camera

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.riski.mystorylife.R
import com.riski.mystorylife.databinding.ActivityCameraBinding
import com.riski.mystorylife.helper.createFile
import com.riski.mystorylife.helper.showMesage
import com.riski.mystorylife.ui.upload.UploadActivity.Companion.CAMERA_X_RESULT

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var captureImg: ImageCapture? = null
    private var selectCamera: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgCamera.setOnClickListener { takeImg() }
        binding.cameraSwitch.setOnClickListener {
            selectCamera = if (selectCamera == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA  else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideUi()
        startCamera()
    }

    private fun takeImg() {
        val imgCapture = captureImg ?: return
        val myFile = createFile(application)
        val output = ImageCapture.OutputFileOptions.Builder(myFile).build()
        imgCapture.takePicture(
            output, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    showMesage(this@CameraActivity, getString(R.string.invalid_img))
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", myFile)
                    intent.putExtra(
                        "cameraBack", selectCamera == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val provider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }
            captureImg = ImageCapture.Builder().build()

            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    this, selectCamera, preview, captureImg )
            } catch (exc: Exception) {
                showMesage(this, getString(R.string.camera_failed))
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideUi() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}