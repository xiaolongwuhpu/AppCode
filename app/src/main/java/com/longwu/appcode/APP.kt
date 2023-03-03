package com.longwu.appcode

import android.app.Application
import android.content.Context


class APP : Application() {
    private val TAG = "myApplication"
    companion object {
        var mContext: Context?=null

    }
    override fun onCreate() {
        super.onCreate()
        mContext = this
    }


}
