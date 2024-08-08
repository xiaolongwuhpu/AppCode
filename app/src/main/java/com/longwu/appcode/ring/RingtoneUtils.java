package com.longwu.appcode.ring;

import android.content.ContentResolver;
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
import java.io.IOException;
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
    public static void setRingtone(Context context, File ringtoneFile) {
        try {




            ContentValues values = getContentValues(ringtoneFile);
//            Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
//            Uri newUri = context.getContentResolver().insert(uri, values);
            Uri newUri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            Log.d(TAG, "Ringtone set successfully: " + newUri.toString());
        } catch (Exception e) {
            Log.e(TAG, "Failed to set ringtone: ", e);
        }
    }

    public static void setRingtone(Context context, Uri uri) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "MyRingtone.mp3");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_RINGTONES);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            Uri newUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            if (newUri != null) {
                OutputStream outputStream = contentResolver.openOutputStream(newUri);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();
            } else {
                throw new IOException("Failed to create new MediaStore record.");
            }
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        File ringtoneFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES), "MyRingtone.mp3");
//        ContentValues values = getContentValues(ringtoneFile);
//        Uri newUri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
//        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
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

    public static Uri copyRingtoneToRingtonesFolder(Context context, Uri uri, String fileName) throws IOException {
        Uri newUri = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_NOTIFICATIONS);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);

            newUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

            if (newUri != null) {
                OutputStream outputStream = contentResolver.openOutputStream(newUri);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newUri;
    }

    public static File copyRingtoneToRingtonesFolder(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File ringTones = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            if (!ringTones.exists()) {
                ringTones.mkdirs();
            }
            File ringtoneDir = new File(ringTones, "MyRingtone.mp3");
            if (ringtoneDir.exists()) {
                return ringtoneDir;
            }
            OutputStream outputStream = new FileOutputStream(ringtoneDir);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            return ringtoneDir;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

