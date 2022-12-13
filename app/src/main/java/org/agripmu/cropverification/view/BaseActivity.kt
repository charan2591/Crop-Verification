package org.agripmu.cropverification.view

//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion.White
//import androidx.compose.ui.unit.dp

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import org.agripmu.cropverification.R
import java.util.*
import java.util.regex.Pattern

open class BaseActivity : AppCompatActivity() {

    var progressDialog: Dialog? = null
    var conMgr : ConnectivityManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun  setIntent(destination : Class<*>)
    {
        val intent = Intent(this,destination)
        startActivity(intent)
    }

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
}