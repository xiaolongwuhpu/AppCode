package com.longwu.appcode.ring;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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

    public static Uri copyRingtoneToRingtonesFolder(Context context, Uri selectAudioUri, String fileName) throws IOException {
        Uri newUri = null;
        File ringTones;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ringTones = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getPath());
        } else {
            ringTones = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
        }

        if (!ringTones.exists()) {
            ringTones.mkdirs();
        }

        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(selectAudioUri);

        // 创建文件路径
        File file = new File(ringTones, fileName);
        if (file.exists()) {
            // 如果文件已存在，则删除它
            file.delete();
        }

        // 将内容写入文件
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();

        // 检查是否已有相同路径的文件存在
        Uri existingUri = null;
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.MediaColumns._ID},
                MediaStore.MediaColumns.DATA + "=?",
                new String[]{file.getAbsolutePath()},
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // 获取现有文件的 Uri
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            existingUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            cursor.close();
        }

        if (existingUri != null) {
            // 如果文件已经存在，则使用现有的 Uri
            newUri = existingUri;
        } else {
            // 否则插入新的记录
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);

            newUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        }

        if (newUri != null) {
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newUri);
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

