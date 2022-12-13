package org.agripmu.cropverification.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import org.agripmu.cropverification.R

class ImageViewerDialog : DialogFragment() {

    lateinit var imageDetail : AppCompatImageView

    companion object {
        private const val EXTRA_IMAGE_RESOURCE = "extra.image_resource"

        @JvmStatic
        fun newInstance(resource: String) = ImageViewerDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_IMAGE_RESOURCE, resource)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.dialog_imageviewer, container, false)
        imageDetail = view.findViewById<AppCompatImageView>(R.id.codeImg)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val uri = arguments?.getString(EXTRA_IMAGE_RESOURCE, "")?.toUri()
//        imageDetail.setImageURI(arguments?.getString(EXTRA_IMAGE_RESOURCE, "") ?: "")
        imageDetail.setImageURI(uri)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}