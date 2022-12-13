package org.agripmu.cropverification.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun  setIntent(destination : Class<*>)
    {
        val intent = Intent(context,destination)
        startActivity(intent)
    }
}