package com.longwu.appcode.ring;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RingtoneUtils {
    private static final String TAG = "RingtoneUtils";

    public static void exportRingtone(Context context, Uri ringtoneUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(ringtoneUri);
            File outputDir = new File(Environment.getExternalStorageDirectory(), "Ringtones");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, "default_ringtone.mp3");

            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            Log.d(TAG, "Ringtone exported to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Failed to export ringtone: ", e);
        }
    }

    //设置本地音频文件为来电铃声
    // File audioFile = new File(Environment.getExternalStorageDirectory(), "Download/sample_audio.mp3");
    //  RingtoneUtils.setRingtone(this, audioFile);
    public static void setRingtone(Context context, File audioFile) {
        try {
            File ringtoneDir = new File(Environment.getExternalStorageDirectory(), "Ringtones");
            if (!ringtoneDir.exists()) {
                ringtoneDir.mkdirs();
            }
            File ringtoneFile = new File(ringtoneDir, "custom_ringtone.mp3");

            InputStream inputStream = new FileInputStream(audioFile);
            OutputStream outputStream = new FileOutputStream(ringtoneFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            ContentValues values = getContentValues(ringtoneFile);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
            Uri newUri = context.getContentResolver().insert(uri, values);

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);

            Log.d(TAG, "Ringtone set successfully: " + newUri.toString());
        } catch (Exception e) {
            Log.e(TAG, "Failed to set ringtone: ", e);
        }
    }

    private static @NonNull ContentValues getContentValues(File ringtoneFile) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, "Custom Ringtone");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        return values;
    }

    public static Uri getDefaultRingtoneUri(Context context) {
        return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
    }
}

