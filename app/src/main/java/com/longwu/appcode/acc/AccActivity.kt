package com.longwu.appcode.acc

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import com.longwu.appcode.R
import com.longwu.appcode.base.BaseActivity


class AccActivity : BaseActivity() {
    private var TAG = "AccWatcher"
    override fun getContentViewLayoutID(): Int? {
        return R.layout.activity_acc
    }

    override fun initViewsAndEvents() {

        findViewById<Button>(R.id.start).setOnClickListener {
            AccessibilityUtil.checkSetting(this,MyAccessibilityService::class.java)
        }
//        try {
//            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//        } catch (e: Exception) {
//            Log.i(TAG, "start ACTION_ACCESSIBILITY_SETTINGS fail: " + e.message)
//            startActivity(Intent(Settings.ACTION_SETTINGS))
//        } finally {
//            finish()
//        }
    }

    private fun showToast(s: String) {
        Log.e(TAG, s)
    }
}