package org.agripmu.cropverification.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import org.agripmu.cropverification.databinding.ActivityHomeBinding

class HomeActivity :BaseActivity() {

    lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.root)
        title = "Home "
    }

    fun submitInfo(view: View) {
        if (validateInput()) {

            // Input is valid, here send data to your server
            val firstName =  binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val contactNo = binding.etContactNo.text.toString()
//            val etDes = binding.etDes.text.to
            showToast("Data Submitted Successfully")
            setIntent(SurveyMainActivity::class.java )
            // Here you can call you API

        }
    }


    private fun validateInput(): Boolean {

        if (binding.etFirstName.text.toString() == "") {
            binding.etFirstName.error = "Please Enter First Name"
            return false
        }
        if (binding.etLastName.text.toString() == "") {
            binding.etLastName.error = "Please Enter Last Name"
            return false
        }
        if (binding.etEmail.text.toString() == "") {
            binding.etEmail.error = "Please Enter Email"
            return false
        }

        if (binding.etContactNo.text.toString() == "") {
            binding.etContactNo.error = "Please Enter Contact No"
            return false
        }
//        if (binding.etDes.text.toString().equals("")) {
//            binding.etDes.setError("Please Enter Designation")
//            return false
//        }
        // checking the proper email format
        if (!isEmailValid(binding.etEmail.text.toString())) {
            binding.etEmail.error = "Please Enter Valid Email"
            return false
        }

        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}