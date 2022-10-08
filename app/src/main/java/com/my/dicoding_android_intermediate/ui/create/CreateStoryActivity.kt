package com.my.dicoding_android_intermediate.ui.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.databinding.ActivityCreateStoryBinding
import com.my.dicoding_android_intermediate.utils.MediaUtility
import com.my.dicoding_android_intermediate.utils.MediaUtility.reduceFileImage
import com.my.dicoding_android_intermediate.utils.MediaUtility.uriToFile
import com.my.dicoding_android_intermediate.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private var getFile: File? = null
    private var token: String = ""
    private lateinit var currentPathPhoto: String
    private val viewModel: CreateStoryViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: LatLng;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.getAuthToken().collect { authToken ->
                    if (!authToken.isNullOrEmpty()) token = authToken
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getMyLastLocation()
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
    }


    // get data location
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    myLocation = LatLng(location.latitude, location.longitude)
                } else {
                    Toast.makeText(
                        this@CreateStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val file = File(currentPathPhoto).also { getFile = it }
            val os: OutputStream

            // Rotate image to correct orientation
            val bitmap = BitmapFactory.decodeFile(getFile?.path)

            // Convert rotated image to file
            try {
                os = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()

                getFile = file
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        MediaUtility.createTempFile(application).also {
            val photoUri = FileProvider.getUriForFile(
                this,
                "com.my.dicoding_android_intermediate",
                it
            )
            currentPathPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            uriToFile(selectedImg, this).also { getFile = it }

            binding.imageView.setImageURI(selectedImg)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_story_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_check -> {
                uploadStory()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun uploadStory() {
        setLoadingState(true)

        val etDescription = binding.etDescription
        var isValid = true

        // Validation for the description edit text
        if (etDescription.text.toString().isBlank()) {
            etDescription.error = getString(R.string.desc_empty_field_error)
            isValid = false
        }

        // Validation for the image
        if (getFile == null) {
            showSnackbar(getString(R.string.empty_image_error))
            isValid = false
        }

        // Required content is valid and ready to upload
        if (isValid) {
            lifecycleScope.launchWhenStarted {
                launch {
                    val description =
                        etDescription.text.toString().toRequestBody("text/plain".toMediaType())

                    // Get image file and convert to MultiPart
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                    // Collect story location
                    var lat: RequestBody? = null
                    var lon: RequestBody? = null
                    Log.i("Lokasi", myLocation.toString())
                    lat =
                        myLocation.latitude.toString()
                            .toRequestBody("text/plain".toMediaType())
                    lon =
                        myLocation.longitude.toString()
                            .toRequestBody("text/plain".toMediaType())
                    // Uploading information to the server and collecting server response
                    // Making decision based on server response
                    viewModel.uploadImage(token, imageMultipart, description, lat, lon)
                        .collect { response ->
                            response.onSuccess {
                                Toast.makeText(
                                    this@CreateStoryActivity,
                                    getString(R.string.uploadedStory),
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }

                            response.onFailure {
                                setLoadingState(false)
                                showSnackbar(getString(R.string.anErrorOccured))
                            }
                        }
                }
            }

        } else setLoadingState(false)
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            btnCamera.isEnabled = !isLoading
            btnGallery.isEnabled = !isLoading
            etDescription.isEnabled = !isLoading

            viewLoading.animateVisibility(isLoading)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}