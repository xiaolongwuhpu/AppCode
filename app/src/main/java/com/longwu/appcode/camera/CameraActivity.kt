package com.longwu.appcode.camera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.longwu.appcode.R


class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var surfaceView = findViewById<Button>(R.id.surfaceView)

        surfaceView.setOnClickListener {
            startActivity(Intent(this, CameraSurfaceActivity::class.java))
        }

        var glSurfaceView = findViewById<Button>(R.id.gl_surfaceView)
        glSurfaceView.setOnClickListener {
            startActivity(Intent(this, CameraGLSurfaceActivity::class.java))
        }
    }
}