package com.longwu.appcode.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.longwu.appcode.R
import com.longwu.appcode.service.MyMusicService


public class MediaBrowserActivity : AppCompatActivity() {

    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_browser)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e("MyMusicService", "onDestroy..........")

    }

    private val connectionCallback: MediaBrowserCompat.ConnectionCallback =
        object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                Log.e("MyMusicService", "onConnected..........")
                // 连接成功后，获取MediaController
                val token = mediaBrowser!!.sessionToken
                mediaController = MediaControllerCompat(this@MediaBrowserActivity, token)
                MediaControllerCompat.setMediaController(this@MediaBrowserActivity, mediaController)

                // 设置媒体控制器的回调
                mediaController!!.registerCallback(controllerCallback)
            }

            override fun onConnectionSuspended() {
                // 连接暂时中断
                Log.e("MyMusicService", "onConnectionSuspended..........")
            }

            override fun onConnectionFailed() {
                // 连接失败
                Log.e("MyMusicService", "onConnectionFailed..........")
            }
        }

    private val controllerCallback: MediaControllerCompat.Callback =
        object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                // 当播放状态变化时调用，可以在这里更新UI
                Log.e("MyMusicService", "onPlaybackStateChanged..........")
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat) {
                // 当媒体信息变化时调用，可以在这里更新UI
                Log.e("MyMusicService", "onMetadataChanged..........")
            }
        }
    
    fun stop(view: View) {
        Log.e("MyMusicService", "stop..........")
        // 释放MediaBrowser连接
        if (mediaBrowser?.isConnected == true) {
            mediaBrowser?.disconnect()
        }

        val intent = Intent(this, MyMusicService::class.java)
        intent.putExtra("action", 1)
        startService(intent)

        Handler().postDelayed({
            Log.e("MyMusicService", "start..........")
            val intent = Intent(this, MyMusicService::class.java)
            intent.putExtra("action", 1)
            startService(intent)
        }, 3000)

    }
    fun start(view: View) {
//        val intent = Intent(this, MyMusicService::class.java)
//        intent.action = "com.longwu.appcode.service.MyMusicService"
//        stopService(intent)
        mediaBrowser = MediaBrowserCompat(
            this,
            ComponentName(this, MyMusicService::class.java),
            connectionCallback, null
        )
        mediaBrowser!!.connect()


    }
}