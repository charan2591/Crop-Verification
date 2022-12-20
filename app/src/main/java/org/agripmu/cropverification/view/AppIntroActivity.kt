package org.agripmu.cropverification.view

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder
import org.agripmu.cropverification.R

class AppIntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showIntroSlides() {
        manager.setFirstRun()
        val pageOne = SliderPagerBuilder()
            .title(getString(R.string.slide_one_top_text))
            .description(getString(R.string.slide_one_down_text))
            .imageDrawable(R.drawable.logo)
            .bgColor(getColor(R.color.slide_one))
            .build()

        val pageTwo = SliderPagerBuilder()
            .title(getString(R.string.slide_two_top_text))
            .description(getString(R.string.slide_two_down_text))
            .imageDrawable(R.drawable.notebook_with_logo)
            .bgColor(getColor(R.color.slide_two))
            .build()

        val pageThree = SliderPagerBuilder()
            .title(getString(R.string.slide_three_top_text))
            .description(getString(R.string.slide_three_down_text))
            .imageDrawable(R.drawable.bow_classic_brown)
            .bgColor(getColor(R.color.slide_three))
            .build()

        val pageFour = SliderPagerBuilder()
            .title(getString(R.string.slide_four_top_text))
            .description(getString(R.string.slide_four_down_text))
            .imageDrawable(R.drawable.taget_and_arrow)
            .bgColor(getColor(R.color.slide_four))
            .build()

        addSlide(AppIntro2Fragment.newInstance(pageOne))
        addSlide(AppIntro2Fragment.newInstance(pageTwo))
        addSlide(AppIntro2Fragment.newInstance(pageThree))
        addSlide(AppIntro2Fragment.newInstance(pageFour))

        showStatusBar(false)
        setFadeAnimation()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
    }


}