package org.agripmu.cropverification.view

import android.os.Bundle
import android.view.View
import org.agripmu.cropverification.R
import org.agripmu.cropverification.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private  lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding!!.toolbar.root)
        title = getString(R .string.settings)
        showBackArrow()
        hideStartLogo()
        hideSettings()
    }

    fun btnLogout(view: View) {
            showAlertWithYesNo(
                getString(R.string.app_name), getString(R.string.logout_msg),
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                prefClearAll()
                clearStackIntent(this, SplashActivity::class.java)
                finish()
            }
    }
}