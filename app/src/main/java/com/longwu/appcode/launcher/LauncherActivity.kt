package com.longwu.appcode.launcher

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.longwu.appcode.R

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)


//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_HOME_SETTINGS);
//        startActivity(intent);
        val componentName = ComponentName(this, LauncherActivity::class.java)
        val bundle = Bundle()
        bundle.putString(":settings:fragment_args_key", componentName.flattenToString())
        val intent = Intent("android.settings.HOME_SETTINGS")
        intent.addFlags(268435456)
            .putExtra(":settings:fragment_args_key", componentName.flattenToString())
            .putExtra(":settings:show_fragment_args", bundle)
        startActivity(intent)
    }
}