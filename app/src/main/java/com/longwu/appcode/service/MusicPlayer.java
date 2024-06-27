package com.longwu.appcode.service;

import android.media.MediaPlayer;

import java.io.IOException;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;

    public MusicPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void setDataSource(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    // 添加其他控制方法，如seekTo等
}

