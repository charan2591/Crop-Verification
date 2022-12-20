package org.agripmu.cropverification.view

//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion.White
//import androidx.compose.ui.unit.dp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.birjuvachhani.locus.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.agripmu.cropverification.R
import org.agripmu.cropverification.util.Config
import java.util.*
import java.util.regex.Pattern

open class BaseActivity : AppCompatActivity() {

    private var textViewTitle: TextView? = null
    private var textViewTotal: TextView? = null
    private var linearSettings: LinearLayout? = null
    private var imgStartLogo: AppCompatImageView? = null
    var progressDialog: Dialog? = null
    private var actionBar : ActionBar? = null
    var conMgr : ConnectivityManager? = null
//    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private val permissionId = 2
    var latitude = 0.0
    var longitude = 0.0

//    companion object {
//        init {
//            //here goes static initializer code
//             lateinit var sharedPreferences: SharedPreferences
//             lateinit var editor: SharedPreferences.Editor
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val request = LocationRequest.create()
        Intent(this, BaseActivity::class.java).apply {
            putExtra("request", request)
        }
        Locus.setLogging(true)

        setPrefs()
    }

    fun  setIntent(destination : Class<*>)
    {
        val intent = Intent(this,destination)
        startActivity(intent)
    }

  private fun setPrefs() {
        val masterKey: MasterKey = MasterKey.Builder(
            this,
            MasterKey.DEFAULT_MASTER_KEY_ALIAS
        ).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sharedPreferences = EncryptedSharedPreferences.create(
            this,
            Config.PREF_HOME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = sharedPreferences!!.edit()
    }

    fun setFirstRun()
    {
        editor!!.putBoolean(Config.FIRST_TIME,false)
        editor!!.commit()
    }

    fun isFirstRun() = sharedPreferences!!.getBoolean(Config.FIRST_TIME,true)

    protected fun showProgress() {
        if (progressDialog == null) {
            progressDialog = Dialog(this)
        }
        progressDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(R.layout.inflate_progress)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    protected fun dismissProgress() {
        if (isFinishing) return
        if (progressDialog != null) if (progressDialog!!.isShowing) progressDialog!!.dismiss()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        overridePendingTransition(android.R.anim.bounce_interpolator, android.R.anim.cycle_interpolator)
        finish()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar!!.title = ""
        textViewTitle = toolbar.findViewById(R.id.toolbarTitle)
        textViewTotal = toolbar.findViewById(R.id.toolbarTotal)
        linearSettings = toolbar.findViewById(R.id.btnSettings)
        imgStartLogo = toolbar.findViewById(R.id.start_logo)
        actionBar = supportActionBar
    }

    override fun setTitle(title: CharSequence) {
        if (textViewTitle != null) {
            textViewTitle!!.text = Html.fromHtml("<b>$title")
        } else {
            actionBar!!.title = title
        }
    }

    fun hideStartLogo() {
        if (imgStartLogo != null) {
            imgStartLogo!!.visibility = View.GONE
        }
    }

    protected fun showBackArrow() {
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
    }

    protected fun showBottomAlert( message : String?)
    {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        /* on below line we are inflating a layout file which we have created. */
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_alert, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        val txtMessage = view.findViewById<TextView>(R.id.alertMessage)

        txtMessage.text = message

        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    protected fun showBottomAlertWithConfirm()
    {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(this)

        /* on below line we are inflating a layout file which we have created. */
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_del_dialog, null)

        val btnOk = view.findViewById<Button>(R.id.idBtnDelete)
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    protected fun showAlert(
        message: String?
    ) {
        if (isFinishing) return
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok), null)
        builder.setCancelable(false)
        builder.show()

    }

    protected fun showAlertWithListener(
        message: String?,
        listener: DialogInterface.OnClickListener?
    ) {
        if (isFinishing) return
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok), listener)
        builder.setCancelable(false)
        builder.show()
    }

    protected fun showAlertWithYesNo(
        title: String?,
        message: String?,
        listener: DialogInterface.OnClickListener?
    ) {
        if (isFinishing) return
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.yes), listener)
        builder.setNegativeButton(getString(R.string.no), null)
        builder.setCancelable(false)
        builder.show()
    }


    open fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun hideView(view: View) {
        view.visibility = View.GONE
    }

    protected fun disableView(view: View) {
        view.isEnabled = false
    }

    protected fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    protected fun showLog(tag: String?, message: String?) {
        Log.e(tag, message!!)
    }

    protected fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    val isConnectedToInternet: Boolean
        get() {
            if (conMgr != null) {
                val info = conMgr!!.allNetworkInfo
                for (anInfo in info) if (anInfo.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }

    open fun startIntent(context: Context?, className: Class<*>?) {
        startActivity(Intent(context, className))
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    open fun isValidMobile(phone: String): Boolean {
        var check = false
        check = if (!Pattern.matches("[0-5][0-9]{9}", phone))
        {
            if (phone.length < 10 || phone.length > 13) {
                // if(phone.length() != 10) {
                false
                // txtPhone.setError("Not Valid Number");
            } else {
                Patterns.PHONE.matcher(phone).matches()
            }
        } else {
            false
        }

//        check = if (!Pattern.matches("[6-9][0-9]{9}", phone))
//        {
//            if (phone.length < 9 || phone.length > 13) {
//                // if(phone.length() != 10) {
//                false
//                // txtPhone.setError("Not Valid Number");
//            } else {
//                Patterns.PHONE.matcher(phone).matches()
//            }
//        } else {
//            false
//        }
        return check
    }

    fun setSpinner(dataList: List<String>, spinnerView: AppCompatSpinner, selection: Int) {
        val stateAdapter: ArrayAdapter<Any> =
            ArrayAdapter<Any>(this, R.layout.inflate_spinner_row, dataList)
        spinnerView.adapter = stateAdapter
        spinnerView.setSelection(selection)
    }

    fun setLocale(language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)

    }

    fun clearStackIntent(context: Context?, toClass: Class<*>?) {
        val intent = Intent(context, toClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
//        viewTransition()
    }


//    @SuppressLint("MissingPermission")
//    protected fun getLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
//                    val location: Location? = task.result
//                    if (location != null) {
//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        val list: List<Address> =
//                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
//
//                            showAlert( "Latitude\n${list[0].latitude}" +
//                                     "Longitude\n${list[0].longitude}" +
//                                     "Country Name\n${list[0].countryName}" +
//                                     "Locality\n${list[0].locality}" +
//                                     "Address\n${list[0].getAddressLine(0)}")
////
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }



    fun getLastLocation()
    {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
        Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                latitude =  result.location!!.latitude
                longitude = result.location!!.longitude
                val geocoder = Geocoder(this, Locale.getDefault())
                val list: List<Address> = geocoder.getFromLocation(
                    latitude,longitude,1)!!

//                showAlert("Current Latitude : "  + latitude +" \n"
//                        +"Current Longitude : "  + longitude +" \n"
//                        +"Country Name : "  + {list[0].countryName} +" \n"
//                        +"Locality : "  + {list[0].locality} +" \n"
//                        +"Current Address : "  + {list[0].getAddressLine(0)} +" \n")
                showAlert("Current Latitude : "  + latitude +" \n"
                        +"Current Longitude : "  + longitude +" \n")

                showLog("lat", result.location!!.latitude.toString())
                showLog("lon", result.location!!.longitude.toString())
            }
            result.error?.let {
                when
                {
                    result.error!!.isDenied ->
                    {
                        Log.e("GPS Result : ","Denied")
                    }
                    result.error!!.isPermanentlyDenied ->
                    {
                        Log.e("GPS Result : ","PermanentlyDenied")
                    }
                    result.error!!.isSettingsResolutionFailed ->
                    {
                        Log.e("GPS Result : ","SettingsResolutionFailed")
                        getLastLocation()
                    }
                    result.error!!.isSettingsDenied ->
                    {
                        Log.e("GPS Result : ","SettingsDenied")
                        getLastLocation()
                    }
                    result.error!!.isFatal ->
                    {
                        Log.e("GPS Result : ","Fatal")
                    }
                }
            }
        }

            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
}