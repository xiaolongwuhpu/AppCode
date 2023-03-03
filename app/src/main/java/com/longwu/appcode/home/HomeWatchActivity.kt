package com.longwu.appcode.home

import android.util.Log
import com.longwu.appcode.R
import com.longwu.appcode.base.BaseActivity
import com.longwu.appcode.home.HomeWatcherUtil.OnHomePressedListener
class HomeWatchActivity : BaseActivity() {
    private var TAG = "HomeWatcherUtil"
    override fun getContentViewLayoutID(): Int? {
        return R.layout.activity_home_watch
    }
    val mHomeWatcher = HomeWatcherUtil(this)
    override fun initViewsAndEvents() {

    }

    override fun onResume() {
        super.onResume()
        showToast("onResume")
        mHomeWatcher.setOnHomePressedListener(object : OnHomePressedListener {
            override fun onHomePressed() {
                showToast("press home")
                //按了HOME键
            }
            override fun onHomeLongPressed() {
                showToast("long press home")
                //长按HOME键
            }
        })
        mHomeWatcher.startWatch()
    }

    override fun onStop() {
        super.onStop()
        showToast("onStop")
        if(mHomeWatcher != null)
            mHomeWatcher.stopWatch();// 在销毁时停止监听，不然会报错的。
    }

    private fun showToast(s: String) {
        Log.e(TAG, s)
    }
}