package org.agripmu.cropverification.view

import android.os.Bundle
import android.view.View
import org.agripmu.cropverification.databinding.ActivityHomeBinding
import org.agripmu.cropverification.databinding.ActivityLoginBinding

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
        setIntent(SurveyMainActivity::class.java)
    }
}