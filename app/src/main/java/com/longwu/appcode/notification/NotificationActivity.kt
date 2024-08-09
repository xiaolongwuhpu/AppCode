package com.longwu.appcode.notification

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.longwu.appcode.R
import com.longwu.appcode.util.NotificationUtils


class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        var show_notification = findViewById<Button>(R.id.show_notification)

        show_notification.setOnClickListener {
            NotificationUtils.showNotification(this, "Hello", "This is a notification message");
        }
    }
}