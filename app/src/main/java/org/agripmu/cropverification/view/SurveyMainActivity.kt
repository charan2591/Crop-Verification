package org.agripmu.cropverification.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.IntentUtils
import com.google.android.material.snackbar.Snackbar
import org.agripmu.cropverification.util.ImageFileUtil
import org.agripmu.cropverification.R
import org.agripmu.cropverification.databinding.ActivityMainBinding
import org.agripmu.cropverification.util.ImageViewerDialog
import org.agripmu.cropverification.util.IntentUtil
import org.agripmu.cropverification.util.MyWorker
import java.io.File
import java.io.IOException
import java.util.*

class SurveyMainActivity : BaseActivity(){

    companion object {
        private const val GITHUB_REPOSITORY = "https://github.com/Dhaval2404/ImagePicker"
        private const val PROFILE_IMAGE_REQ_CODE = 101
        private const val GALLERY_IMAGE_REQ_CODE = 102
        private const val CAMERA_IMAGE_REQ_CODE = 103

        const val TAG = "SurveyMainActivity"
        const val NOTIFICATION_MESSAGE_TAG = "message from notification"
        fun newIntent(context: Context) = Intent(context, SurveyMainActivity::class.java).apply {
            putExtra(
                NOTIFICATION_MESSAGE_TAG, "Hi ☕\uD83C\uDF77\uD83C\uDF70"
            )

        }
    }

    private lateinit var binding: ActivityMainBinding
    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null

    val workerRequest = OneTimeWorkRequestBuilder<MyWorker>().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        title = getString(R.string.app_name)
        showBackArrow()
        binding.contentMain.contentProfile.imgProfile
            .setDrawableImage(R.drawable.ic_person, true)

        hideStartLogo()
    }


        private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
                isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
//            sendNotification(this)

                workerFunc()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }


        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_github -> {
                IntentUtil.openURL(this, GITHUB_REPOSITORY)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                mProfileUri = fileUri
                imgProfile.setLocalImage(fileUri, true)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }*/

    @Suppress("UNUSED_PARAMETER")
    fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            // Crop Square image
            .galleryOnly()
            .cropSquare()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }

            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")
            }
            // Image resolution will be less than 512 x 512
            .maxResultSize(512, 512)
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickGalleryImage(view: View) {
        ImagePicker.with(this)
            // Crop Image(User can choose Aspect Ratio)
            .crop()
            .compress(200)
            // User can only select image from Gallery
            .galleryOnly()

            .galleryMimeTypes( // no gif images at all
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            // Image resolution will be less than 1080 x 1920
            .maxResultSize(1080, 1920)
            // .saveDir(getExternalFilesDir(null)!!)
            .start(GALLERY_IMAGE_REQ_CODE)
    }

    /**
     * Ref: https://gist.github.com/granoeste/5574148
     */
    @Suppress("UNUSED_PARAMETER")
    fun pickCameraImage(view: View) {

//            val appDirctory =File(Environment.getExternalStorageDirectory().path+ "/ImagePicker")
            val appDirctory =File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path + "/ImagePicker")
            if (!appDirctory.exists())
            appDirctory.mkdirs()

            ImagePicker.with(this)
            // User can only capture image from Camera
            .compress(500)
            .cameraOnly()
//            .maxResultSize(1080, 1920)
            .maxResultSize(3080, 4920)

            // Image size will be less than 1024 KB
            // .compress(1024)
            //  Path: /storage/sdcard0/Android/data/package/files
                .saveDir(appDirctory)
//            .saveDir(getExternalFilesDir(null)!!)
//            //  Path: /storage/sdcard0/Android/data/package/files/DCIM
//            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
//            //  Path: /storage/sdcard0/Android/data/package/files/Download
//            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
//            //  Path: /storage/sdcard0/Android/data/package/files/Pictures
//            .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
//            //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
//            .saveDir(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
//            //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
//            .saveDir(getExternalFilesDir("ImagePicker")!!)
//            //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
//            .saveDir(File(getExternalCacheDir(), "ImagePicker"))
//            //  Path: /data/data/package/cache/ImagePicker
//            .saveDir(File(getCacheDir(), "ImagePicker"))
//            //  Path: /data/data/package/files/ImagePicker
//            .saveDir(File(getFilesDir(), "ImagePicker"))

            // Below saveDir path will not work, So do not use it
            //  Path: /storage/sdcard0/DCIM
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
            //  Path: /storage/sdcard0/Pictures
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
            //  Path: /storage/sdcard0/ImagePicker
            //  .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))

            .start(CAMERA_IMAGE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            when (requestCode) {
                PROFILE_IMAGE_REQ_CODE -> {
                    mProfileUri = uri
                    binding.contentMain.contentProfile.imgProfile.setLocalImage(uri, true)
                }
                GALLERY_IMAGE_REQ_CODE -> {
                    mGalleryUri = uri
                    binding.contentMain.contentGalleryOnly.imgGallery.setLocalImage(uri)
                }
                CAMERA_IMAGE_REQ_CODE -> {
                    mCameraUri = uri
                    binding.contentMain.contentCameraOnly.imgCamera.setLocalImage(uri)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showImageCode(view: View) {
//    workerFunc()

        requestNotificationsPermissions()
        if(mProfileUri != null
            || mCameraUri != null
            ||mGalleryUri != null) {

            val resource = when (view) {
                binding.contentMain.contentProfile.imgProfileCode -> mProfileUri.toString()
                binding.contentMain.contentCameraOnly.imgCameraCode -> mCameraUri.toString()
                binding.contentMain.contentGalleryOnly.imgGalleryCode -> mGalleryUri.toString()
                else -> 0
            }
            ImageViewerDialog.newInstance(resource as String).show(supportFragmentManager, "")
        }
        else {
            showToast("Image Not Found")
        }
    }

    fun showImage(view: View) {
        val uri = when (view) {
            binding.contentMain.contentProfile.imgProfile -> mProfileUri
            binding.contentMain.contentCameraOnly.imgCamera -> mCameraUri
            binding.contentMain.contentGalleryOnly.imgGallery -> mGalleryUri
            else -> null
        }

        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(this, uri))
        }
    }

    fun showImageInfo(view: View) {
        getLastLocation()
        val latlng = "LatLng : " + latitude.toString() +
                " , " +longitude.toString()
        binding.contentMain.txtCurrentLatLng.text = latlng

        getAddress(latitude, longitude)

        val uri = when (view) {
            binding.contentMain.contentProfile.imgProfileInfo -> mProfileUri
            binding.contentMain.contentCameraOnly.imgCameraInfo -> mCameraUri
            binding.contentMain.contentGalleryOnly.imgGalleryInfo -> mGalleryUri
            else -> null
        }

        AlertDialog.Builder(this)
            .setTitle("Image Info")
            .setMessage(ImageFileUtil.getFileInfo(this, uri))
            .setPositiveButton("Ok", null)
            .show()
    }

    private fun ImageView.setDrawableImage(@DrawableRes resource: Int, applyCircle: Boolean = false) {
        val glide = Glide.with(this).load(resource)
        if (applyCircle) {
            glide.apply(RequestOptions.circleCropTransform()).into(this)
        } else {
            glide.into(this)
        }
    }

    private fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
        val glide = Glide.with(this).load(uri)
        if (applyCircle) {
            glide.apply(RequestOptions.circleCropTransform()).into(this)
        } else {
            glide.into(this)
        }
    }

    private fun workerFunc(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workerRequest = OneTimeWorkRequestBuilder<MyWorker>().setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueue(workerRequest)

//        val workerRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
//        WorkManager.getInstance(this).enqueue(workerRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workerRequest.id)
            .observe(this, Observer {
                val workerStatus = it.state.name
                showToast(workerStatus)
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestNotificationsPermissions(){

        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                Log.e(TAG, "onCreate: PERMISSION GRANTED")
//                sendNotification(this)
                workerFunc()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                   binding.contentMain.contentProfile.fabAddPhoto,
                    "Notification blocked",
                    Snackbar.LENGTH_LONG
                ).setAction("Settings") {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            }
            else -> {
                // The registered ActivityResultCallback gets the result of this request
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }

        }

    }

    private fun getAddress(latitude: Double , longitude : Double){
        val geocoder = Geocoder(this, Locale.getDefault())
        val address: Address?
        var fulladdress = ""
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (addresses!!.isNotEmpty()) {
            address = addresses!![0]
            fulladdress = address!!.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            var city = address!!.locality;
            var state = address!!.getAdminArea();
            var country = address!!.getCountryName();
            var postalCode = address!!.getPostalCode();
            var knownName = address!!.getFeatureName(); // Only if available else return NULL

            showAlert("City :  "  + city +"  "+ "State :  "  + state
                    +"Country :  "  + country + "postal Code  :  "  + postalCode
            + "known Name :  "  + knownName)
        } else{
            fulladdress = "Location not found"
            showToast("Location not found")
        }
    }

}