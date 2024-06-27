package com.longwu.appcode.wallpaper

import android.Manifest
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.longwu.appcode.R
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_video
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_wallpaper
import java.io.IOException


class LiveWallpaperMainActivity : AppCompatActivity() {

    private val PICK_IMAGE = 1
    private val PICK_VIDEO = 2
    private val PERMISSION_REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_wallpaper_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkAndRequestPermissions()
        btn_set_wallpaper.setOnClickListener {
            chooseImage(it)
        }

        btn_set_video.setOnClickListener {
            chooseVideo(it)
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SET_WALLPAPER
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SET_WALLPAPER
                    ), PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
            } else {
                // 权限被拒绝
            }
        }
    }

    fun chooseImage(view: View?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, PICK_IMAGE)
    }

    fun chooseVideo(view: View?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("video/*")
        startActivityForResult(intent, PICK_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            setWallpaperFromUri(imageUri)
        } else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            val videoUri = data.data
            setVideoWallpaper(videoUri)
        }
    }

    private fun setWallpaperFromUri(imageUri: Uri?) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val wallpaperManager = WallpaperManager.getInstance(this)
            wallpaperManager.setBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setVideoWallpaper(videoUri: Uri?) {
        if (videoUri != null) {
            VideoWallpaperService.setVideoUri(videoUri)
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, VideoWallpaperService::class.java)
            )
            startActivity(intent)
        } else {
            Log.e("LiveWallpaper", "Video URI is null")
        }
    }

}