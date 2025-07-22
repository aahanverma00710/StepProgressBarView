package com.avcoding.stepprogressbarview

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavShowcaseHelper(
    private val activity: Activity,
    private val bottomNavView: BottomNavigationView,
    private val targetItemId: Int,
    private val tooltipLayoutRes: Int,
    private val tooltipGravity: CustomShowcaseView.Gravity = CustomShowcaseView.Gravity.TOP,
    private val onDismiss: () -> Unit = {}
) {

    fun show() {
        val targetView = getBottomNavItemView(bottomNavView, targetItemId) ?: return

        // Wait until layout is ready
        targetView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                CustomShowcaseView.Builder(activity)
                    .setTargetView(targetView)
                    .setCustomTooltip(tooltipLayoutRes)
                    .setTooltipGravity(tooltipGravity)
                    .onDismiss(onDismiss)
                    .build()
                    .show()
            }
        })
    }

    private fun getBottomNavItemView(bottomNav: BottomNavigationView, itemId: Int): View? {
        val menuView = bottomNav.getChildAt(0) as? ViewGroup ?: return null
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i)
            if (bottomNav.menu.getItem(i).itemId == itemId) {
                return item
            }
        }
        return null
    }
}
