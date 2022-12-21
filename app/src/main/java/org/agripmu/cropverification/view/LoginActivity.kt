package org.agripmu.cropverification.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import org.agripmu.cropverification.R
import org.agripmu.cropverification.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.toolbar.title = fragmentName
        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        hideView(binding.edtOtp)
        hideView(binding.btnOtp)
        showView(binding.btnUsername)
    }

    fun submitMobile(view: View)
    {
        hideKeyboard(this, binding!!.edtUsername)
        when {
            TextUtils.isEmpty(binding!!.edtUsername.text.toString().trim()) -> {
                showBottomAlert(getString(R.string.mobile_required))
            }
            binding!!.edtUsername.text.toString().trim().length != 10 -> {
                showBottomAlert(getString(R.string.valid_mobile_required))
            }
            !isValidMobile(binding!!.edtUsername.text.toString().trim()) -> {
                showBottomAlert(getString(R.string.invalid_mobile))
            }
            else -> {
                showView(binding.edtOtp)
                showView(binding.btnOtp)
                hideView(binding.btnUsername)
            }

        }

    }

    fun login(view: View) {

        hideKeyboard(this, binding!!.edtOtp)
        when {
            TextUtils.isEmpty(binding!!.edtOtp.text.toString().trim()) -> {
                showBottomAlert(getString(R.string.otp_required))
            }
            binding!!.edtOtp.text.toString().trim().length != 6 -> {
                showBottomAlert(getString(R.string.valid_otp_required))
            }
            else -> {
                showProgress()
                setIntent(HomeActivity::class.java)
                dismissProgress()
            }
        }

    }
}