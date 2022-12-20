package com.longwu.appcode.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    var mContext: Context? = null
    var TAG=BaseActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        getContentViewLayoutID()?.apply {
            setContentView(this)
        }
        initViewsAndEvents()
    }

    @LayoutRes
    protected abstract fun getContentViewLayoutID(): Int?

    protected abstract fun initViewsAndEvents()

}