package com.avcoding.stepprogressbarview

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class ShowcaseOverlay(
    private val activity: Activity,
    private val onDismiss: () -> Unit = {}
) {
    private lateinit var overlay: View

    fun show() {
        val root = activity.window.decorView as ViewGroup
        overlay = LayoutInflater.from(activity)
            .inflate(R.layout.layout_showcase_overlay, root, false)

        val btnGotIt = overlay.findViewById<Button>(R.id.btnGotIt)
        btnGotIt.setOnClickListener {
            dismiss()
        }

        root.addView(overlay)
    }

    fun dismiss() {
        val root = activity.window.decorView as ViewGroup
        root.removeView(overlay)
        onDismiss()
    }
}
