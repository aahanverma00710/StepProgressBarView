package com.avcoding.stepprogressbarview

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowCaseTestingActivity : AppCompatActivity() {

    var scope = CoroutineScope(Job()+Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_case)

        showShowCaseView()
    }

    private fun showShowCaseView() {
        scope.launch {
            delay(1000)
            CustomShowcaseView.Builder(this@ShowCaseTestingActivity)
                .setTargetView(findViewById(R.id.progressView))
                .setCustomTooltip(R.layout.layout_showcase_overlay)
                .setTooltipGravity(CustomShowcaseView.Gravity.BOTTOM)
                .onDismiss {
                    // Optionally show next tip
                    showOnBottomNavigation()
                }
                .build()
                .show()
            //showTooltipBelowOrAbove(this@ShowCaseTestingActivity, findViewById(R.id.progressView),false)
        }
    }

    fun showTooltipBelowOrAbove(context: Activity, anchorView: View, showAbove: Boolean = false) {
        val root = context.window.decorView as ViewGroup
        val tooltipView = LayoutInflater.from(context).inflate(R.layout.layout_showcase_overlay, root, false)
        val tooltipContainer = tooltipView.findViewById<View>(R.id.tooltipContainer)
        val btnGotIt = tooltipView.findViewById<Button>(R.id.btnGotIt)

        btnGotIt.setOnClickListener {
            root.removeView(tooltipView)
        }

        val anchorLocation = IntArray(2)
        anchorView.getLocationOnScreen(anchorLocation)

        val rootLocation = IntArray(2)
        root.getLocationOnScreen(rootLocation)

        val anchorTop = anchorLocation[1] - rootLocation[1]
        val anchorBottom = anchorTop + anchorView.height

        tooltipView.post {
            val params = tooltipContainer.layoutParams as FrameLayout.LayoutParams

            if (showAbove) {
                tooltipContainer.measure(
                    View.MeasureSpec.makeMeasureSpec(root.width, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.UNSPECIFIED
                )
                val tooltipHeight = tooltipContainer.measuredHeight
                params.topMargin = (anchorTop - tooltipHeight - 16).coerceAtLeast(0)
            } else {
                params.topMargin = anchorBottom + 16
            }

            tooltipContainer.layoutParams = params
        }

        root.addView(tooltipView)
    }


    fun showTooltip(context: Activity, anchorView: View) {
        val root = context.window.decorView as ViewGroup
        val tooltipView = LayoutInflater.from(context).inflate(R.layout.layout_showcase_overlay, root, false)

        val tooltipContainer = tooltipView.findViewById<LinearLayout>(R.id.tooltipContainer)
        val btnGotIt = tooltipView.findViewById<Button>(R.id.btnGotIt)

        btnGotIt.setOnClickListener {
            root.removeView(tooltipView)
        }

        // Get position of anchor view on screen
        val anchorLocation = IntArray(2)
        anchorView.getLocationOnScreen(anchorLocation)
        val anchorY = anchorLocation[1] + anchorView.height

        val rootLocation = IntArray(2)
        root.getLocationOnScreen(rootLocation)

        val relativeMarginTop = anchorY - rootLocation[1] + 16 // 16dp spacing

        val params = tooltipContainer.layoutParams as FrameLayout.LayoutParams
        params.topMargin = relativeMarginTop
        tooltipContainer.layoutParams = params

        root.addView(tooltipView)
    }


    fun getBottomNavItemView(bottomNav: BottomNavigationView, @IdRes itemId: Int): View? {
        val menuView = bottomNav.getChildAt(0) as? ViewGroup ?: return null

        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i)
            if (item is View && bottomNav.menu.getItem(i).itemId == itemId) {
                return item
            }
        }
        return null
    }

    fun showOnBottomNavigation() = GlobalScope.launch(Dispatchers.Main) {
        delay(1000)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav?.let {
            CustomShowcaseView.Builder(this@ShowCaseTestingActivity)
                .setTargetView(bottomNav)
                .setCustomTooltip(R.layout.bottom_nav_tool_top)
                .setTooltipGravity(CustomShowcaseView.Gravity.TOP)
                .onDismiss {
                    // next step or dismiss logic
                }
                .build()
                .show()
        }
    }
}