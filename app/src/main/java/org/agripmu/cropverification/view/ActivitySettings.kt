package org.agripmu.cropverification.view

import android.os.Bundle
import org.agripmu.cropverification.R
import org.agripmu.cropverification.databinding.ActivitySettingsBinding

class ActivitySettings : BaseActivity() {

    private  lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding!!.adtToolbar.root)
        title = getString(R .string.settings)
        showBackArrow()
    }
}