package org.agripmu.cropverification.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder
import org.agripmu.cropverification.R
import org.agripmu.cropverification.util.Config

class AppIntroActivity : AppIntro() {

    private lateinit var manager: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = BaseActivity()

        manager.hideStartLogo()

        val masterKey: MasterKey = MasterKey.Builder(
            this,
            MasterKey.DEFAULT_MASTER_KEY_ALIAS
        ).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        manager.sharedPreferences = EncryptedSharedPreferences.create(
            this,
            Config.PREF_HOME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        manager.editor = manager.sharedPreferences!!.edit()

        if(manager.isFirstRun()){
            showIntroSlides()
        }
        else
        {
            goToMain()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showIntroSlides() {
        manager.setFirstRun()
        val pageOne = SliderPagerBuilder()
            .title(getString(R.string.slide_one_top_text))
            .description(getString(R.string.slide_one_down_text))
            .imageDrawable(R.drawable.introduction_icon)
            .bgColor(getColor(R.color.slide_one))
            .build()

        val pageTwo = SliderPagerBuilder()
            .title(getString(R.string.slide_two_top_text))
            .description(getString(R.string.slide_two_down_text))
            .imageDrawable(R.drawable.benefits_icon_a)
            .bgColor(getColor(R.color.slide_two))
            .descTypeface()
            .build()

        val pageThree = SliderPagerBuilder()
            .title(getString(R.string.slide_three_top_text))
            .description(getString(R.string.slide_three_down_text))
            .imageDrawable(R.drawable.facility_icon)
            .bgColor(getColor(R.color.slide_three))
            .build()

        val pageFour = SliderPagerBuilder()
            .title(getString(R.string.slide_four_top_text))
            .description(getString(R.string.slide_four_down_text))
            .imageDrawable(R.drawable.outcome_icon)
            .bgColor(getColor(R.color.slide_four))
            .build()

        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(AppIntro2Fragment.newInstance(pageTwo))
        addSlide(AppIntro2Fragment.newInstance(pageThree))
        addSlide(AppIntro2Fragment.newInstance(pageFour))

        showStatusBar(false)

//        setFadeAnimation()
//        setZoomAnimation()
//        setFlowAnimation()
//        setSlideOverAnimation()
        setDepthAnimation()
    }

    private fun goToMain() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        Log.d("Hello", "Changed")
    }


}