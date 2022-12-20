package com.longwu.appcode

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.longwu.appcode.appwidget.AppWidgetManager
import com.longwu.appcode.launcher.LauncherActivity
import com.longwu.appcode.permission.PermissionActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_widget).setOnClickListener(View.OnClickListener {
            AppWidgetManager.instance.canShowWidget(this,null)
        })

        findViewById<Button>(R.id.btn_launcher).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LauncherActivity::class.java))
        })

        findViewById<Button>(R.id.btn_permission).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
        })
    }

}
