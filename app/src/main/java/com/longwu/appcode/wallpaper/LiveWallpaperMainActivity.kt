package com.longwu.appcode.wallpaper

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.longwu.appcode.R
import com.longwu.appcode.ring.RingtoneUtils
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_live_camera
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_live_camera2
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_live_cameraX
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_live_wallpaper
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_phone_ringtone
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_video
import kotlinx.android.synthetic.main.activity_live_wallpaper_main.btn_set_wallpaper
import java.io.IOException


class LiveWallpaperMainActivity : AppCompatActivity() {
    private val TAG = "LiveWallpaper"
    private val PICK_IMAGE = 1
    private val PICK_VIDEO = 2
    private val REQUEST_RINGTONE = 3
    private val PERMISSION_REQUEST_CODE = 100
    private val PERMISSION_REQUEST_CODE_SETTING = 101
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

        btn_set_live_wallpaper.setOnClickListener {
            setLiveWallpaper()
        }
        btn_set_live_camera.setOnClickListener {
            setLiveCamera()
        }
        btn_set_live_camera2.setOnClickListener {
            setLiveCamera2()
        }
        btn_set_live_cameraX.setOnClickListener {
            setLiveCameraX()
        }
        btn_set_phone_ringtone.setOnClickListener {
            chooseRingtone()
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER, Manifest.permission.CAMERA, Manifest.permission.WRITE_SETTINGS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun checkAndRequestPermissionsSetting() : Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:$packageName")
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivityForResult(intent, PERMISSION_REQUEST_CODE_SETTING)
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                Log.e("LiveWallpaper", "permission granted")
            } else {
                // 权限被拒绝
                Log.e("LiveWallpaper", "permission not granted")
            }
        }else if (requestCode == PERMISSION_REQUEST_CODE_SETTING) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                Log.e("LiveWallpaper", "permission granted")
            } else {
                // 权限被拒绝
                Log.e("LiveWallpaper", "permission not granted")
            }
        }
    }

    private fun chooseImage(view: View?) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun chooseVideo(view: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_VIDEO)
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.setType("video/*")
//        startActivityForResult(intent, PICK_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            setWallpaperFromUri(imageUri)
        } else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null) {
            val videoUri = data.data
            setVideoWallpaper(videoUri)
        } else if (requestCode === REQUEST_RINGTONE && resultCode === RESULT_OK && data != null) {
            var uri = data.data
//            var audioFile = RingtoneUtils.copyRingtoneToRingtonesFolder(this, uri)
//            RingtoneUtils.setRingtone(this, audioFile)
            //根据uri获取文件文件名字
            var name = getFileNameFromUri(uri)
            try {
                val newUri = RingtoneUtils.copyRingtoneToRingtonesFolder(this,uri,name)
                setRingtone(newUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(uri: Uri?): String? {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        var displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
        cursor.close()
        return displayName
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

    private fun setLiveWallpaper() {
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, CubeWallpaper1::class.java)
            )
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setLiveCamera() {
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, CameraWallpaperService::class.java)
            )
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setLiveCamera2() {
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, Camera2WallpaperService::class.java)
            )
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setLiveCameraX() {
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this, CameraXWallpaperService::class.java)
            )
            startActivity(intent)
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
            intent.putExtra("videoUri", videoUri.toString())
            startActivity(intent)
            Log.e("LiveWallpaper", "Video wallpaper set")
        } else {
            Log.e("LiveWallpaper", "Video URI is null")
        }
    }


    private fun chooseRingtone() {
        if (!checkAndRequestPermissionsSetting()) {
            return
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        startActivityForResult(intent, REQUEST_RINGTONE)
    }

    private fun setRingtone(uri: Uri) {
        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri)
        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, uri)
    }
}