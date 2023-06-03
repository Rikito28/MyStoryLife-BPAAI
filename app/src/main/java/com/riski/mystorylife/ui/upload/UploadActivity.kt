package com.riski.mystorylife.ui.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.mystorylife.R
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.data.model.UserDataStory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivityUploadBinding
import com.riski.mystorylife.helper.*
import com.riski.mystorylife.ui.camera.CameraActivity
import com.riski.mystorylife.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")
    private lateinit var binding: ActivityUploadBinding

    private lateinit var user: UserDataStory
    private var getFile: File? = null
    private lateinit var uploadViewModel: UploadViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if  (requestCode == REQUEST_CODE_PERMISSIONS) {
            if(!allPermissionGrant()) {
                Toast.makeText(this, getString(R.string.invalid), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionGrant() = REQUIRES_PERMISSION.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUploadBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.upload_app)
            setDisplayHomeAsUpEnabled(true)
        }

        uploadViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore), this))[UploadViewModel::class.java]

        loadingBar()
        getPermission()

        binding.cameraUpload.setOnClickListener { startCameraX() }
        binding.galeryUpload.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadImg() }
    }

    private fun getPermission() {
        if (!allPermissionGrant()) {
            ActivityCompat.requestPermissions(this, REQUIRES_PERMISSION, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun loadingBar() {
        uploadViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    btnUpload.visibility = View.INVISIBLE
                } else progressBar.visibility = View.GONE
                }
            }
        }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun startCameraX() {
        intentCamera.launch(Intent(this, CameraActivity::class.java))
    }

    private val intentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateBitmap(file, isBackCamera)
                getFile = myFile
                binding.previewUpload.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose))
        intentGallery.launch(chooser)
    }

    private val intentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val imgSelect: Uri = it.data?.data as Uri
            val myFile  = uriToFile(imgSelect, this@UploadActivity)
            getFile = myFile
            binding.previewUpload.setImageURI(imgSelect)
        }
    }

    private fun uploadImg() {
        if (getFile !=null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.deskUpload.text.toString().toRequestBody("text/plan".toMediaType())
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imgMultipart = MultipartBody.Part.createFormData("photo",file.name,requestFile)

            uploadViewModel.getUser().observe(this) { uploadres ->
                this.user = uploadres
                uploadViewModel.uploadImg(user.token, description, imgMultipart)
                uploadViewModel.upload.observe(this@UploadActivity) { response ->
                    if (!response.error) {
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(this@UploadActivity, response.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@UploadActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                } else {
                    loadingBar()
                    Toast.makeText(this@UploadActivity, response.message, Toast.LENGTH_SHORT).show()
                }
                }
            }
        } else {
            showMesage(this@UploadActivity, getString(R.string.no_file))
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRES_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }
}