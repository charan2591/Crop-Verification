package org.agripmu.cropverification.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
        val decorView = window.decorView
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

//        window.statusBarColor = ContextCompat.getColor(this, R.color.colorName)

        manager = BaseActivity()
        manager.hideStartLogo()
        manager.setPrefs(this)

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
        val pageOne = SliderPagerBuilder()
            .title(getString(R.string.slide_one_top_text))
            .description(getString(R.string.slide_one_down_text))
            .imageDrawable(R.drawable.introduction_icon_b)
            .bgColor(getColor(R.color.slide_one))
            .build()

        val pageTwo = SliderPagerBuilder()
            .title(getString(R.string.slide_two_top_text))
            .description(getString(R.string.slide_two_down_text))
            .imageDrawable(R.drawable.benefits_icon_a)
            .bgColor(getColor(R.color.slide_two))
            .build()

        val pageThree = SliderPagerBuilder()
            .title(getString(R.string.slide_three_top_text))
            .description(getString(R.string.slide_three_down_text))
            .imageDrawable(R.drawable.facility_c)
            .bgColor(getColor(R.color.slide_three))
            .build()

        val pageFour = SliderPagerBuilder()
            .title(getString(R.string.slide_four_top_text))
            .description(getString(R.string.slide_four_down_text))
            .imageDrawable(R.drawable.outcome_icon_aa)
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
        manager.setFirstRun()
        goToMain()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        Log.d("Hello", "Changed")
    }


}