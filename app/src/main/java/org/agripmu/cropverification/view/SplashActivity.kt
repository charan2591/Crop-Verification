package org.agripmu.cropverification.view

import android.content.Intent
import android.os.Bundle
import org.agripmu.cropverification.MainActivity


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setIntent(LoginActivity::class.java)

        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}