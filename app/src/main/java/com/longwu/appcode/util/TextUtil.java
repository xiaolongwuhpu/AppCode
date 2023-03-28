package com.longwu.appcode.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TextUtil {

    public static ByteBuffer loadFbsFileFromAssets(Context context, String pathName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(pathName);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        return ByteBuffer.wrap(buffer);
    }

    public static ByteBuffer loadFbsFileFromFile(Context context, String pathName) throws IOException {
        FileInputStream fis = new FileInputStream(pathName);
        try {
            byte[] buffer = new byte[(int) new File(pathName).length()];
            fis.read(buffer);
            return ByteBuffer.wrap(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {

            }
        }
        return null;
    }

    public static byte[] loadFileFromFile(String pathName) throws IOException {
        FileInputStream fis = new FileInputStream(pathName);
        try {
            byte[] buffer = new byte[(int) new File(pathName).length()];
            fis.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {

            }
        }
        return null;
    }


    public static String getAssetResourceText(Context context, String path, String asset) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        try {
            is = context.getAssets().open(path + "/" + asset);
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int count = -1;
            while ((count = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, count);
            }

            return baos.toString("utf-8");
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static List<String> getAssetResourceLines(Context context, String path, String asset) {
        InputStream is = null;
        BufferedReader bufferedReader = null;
        try {
            is = context.getAssets().open(path + "/" + asset);
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            List<String> result = new ArrayList<String>();
            String lineStr = null;
            while ((lineStr = bufferedReader.readLine()) != null) {
                lineStr = lineStr.trim();
                if (lineStr.length() > 0) {
                    result.add(lineStr);
                }
            }

            return result;
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static JSONObject getJsonObjectFromFile(Context context, String fileName) {
        String fileContent = getAssetResourceText(context, "raw", fileName);
        if (fileContent != null && fileContent.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(fileContent);
                return jsonObject;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
