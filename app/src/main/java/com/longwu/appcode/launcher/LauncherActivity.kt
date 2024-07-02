package com.longwu.appcode.launcher
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.longwu.appcode.R


class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        val componentName = ComponentName(this, LauncherActivity::class.java)
        val bundle = Bundle()
        bundle.putString(":settings:fragment_args_key", componentName.flattenToString())
        val intent = Intent("android.settings.HOME_SETTINGS")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(":settings:fragment_args_key", componentName.flattenToString())
            .putExtra(":settings:show_fragment_args", bundle)
        startActivity(intent)
    }
}