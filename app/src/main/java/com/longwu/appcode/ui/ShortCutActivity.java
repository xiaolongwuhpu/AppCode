//package com.longwu.appcode.ui;
//
//import android.annotation.SuppressLint;
//import android.app.PendingIntent;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ShortcutInfo;
//import android.content.pm.ShortcutManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Icon;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.pm.ShortcutInfoCompat;
//import androidx.core.content.pm.ShortcutManagerCompat;
//import androidx.core.graphics.drawable.IconCompat;
//
//import com.longwu.appcode.MainActivity;
//import com.longwu.appcode.R;
//import com.longwu.appcode.util.TargetSSupport;
//
//import java.net.URL;
//import java.util.Arrays;
//import java.util.Objects;
//
//public class ShortCutActivity extends AppCompatActivity {
//    private static String INTENT_LAUNCH_SHORTCUT = "activitylauncher.intent.action.LAUNCH_SHORTCUT";
//    Context context;
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_short_cut);
//        context=this;
//        findViewById(R.id.btn_short_cut).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    createShortcutIcon();
//                }
//                startCreate();
//            }
//        });
//    }
//
//    private void startCreate() {
//        Log.e("longwu", "0000000000");
//        new AsyncTask<String, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(String... params) {
//                try {
//                    Log.e("longwu", "doInBackground");
//                    URL url = new URL(params[0]);
//                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @SuppressLint("WrongThread")
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                Log.e("longwu", "onPostExecute");
//                if (bitmap != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//                    Log.e("longwu", "333333333");
//                    try {
//                        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
//                        Log.e("longwu", "4444");
//                        // 意图要打开YouTube
//                        Intent openYoutubeIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube"/*, TargetSSupport.INSTANCE.pendingIntentFlagImmutable(PendingIntent.FLAG_UPDATE_CURRENT)*/);
//                        Log.e("longwu", "5555");
//                        ShortcutInfo shortcut = new ShortcutInfo.Builder(ShortCutActivity.this, "id1")
//                                .setShortLabel("Short label")
//                                .setLongLabel("Long label")
//                                .setIcon(Icon.createWithBitmap(bitmap))
////                                .setIntent(openYoutubeIntent)
//                                .setIntent(new Intent(Intent.ACTION_MAIN) {{
//                                    setComponent(new ComponentName(getPackageName(), ShortCutActivity.class.getName()));
//                                }})
//                                .build();
//                        Log.e("longwu", "666");
//                        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
//                        Log.e("longwu", "777");
//
//
//
//                        boolean isSupport = ShortcutManagerCompat.isRequestPinShortcutSupported(ShortCutActivity.this);
//                        Log.e("longwu", "isSupport="+isSupport);
////                        ShortcutInfo shortcut = new ShortcutInfoCompat.Builder(ShortCutActivity.this, "id1")
////                                .setShortLabel("Website")
////                                .setLongLabel("Open the website")
////                                .setIcon(IconCompat.createWithResource(ShortCutActivity.this, R.mipmap.ic_launcher))
////                                .setIntent(new Intent(Intent.ACTION_VIEW) {{
////                                    setComponent(new ComponentName(getPackageName(), ShortCutActivity.class.getName()));
////                                }})
////                                .build().toShortcutInfo();
////
////                        shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
////                        ShortcutManagerCompat.pushDynamicShortcut(ShortCutActivity.this, shortcut);
//                    } catch (Exception e) {
//                        Log.e("longwu", "error:  " + e.toString());
//                    }
//
//
//                } else {
//                    Log.e("longwu", "4444444444");
//                }
//            }
//        }.execute("https://pic.616pic.com/ys_bnew_img/00/52/57/sPtmZ1S2yQ.jpg");
//
//    }
//
//
//
//    /**
//     * 创建快捷图标
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void createShortcutIcon() {
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N_MR1) {
//            Toast.makeText(this, "暂不支持 创建快捷图标", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        ShortcutManager shortcutManager = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
//            shortcutManager = Objects.requireNonNull(getSystemService(ShortcutManager.class));
//        }
//        Toast.makeText(this, "创建快捷图标", Toast.LENGTH_SHORT).show();
//
//        String title = "第二页";
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo_about_us);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setPackage(getPackageName());
////        // 添加Intent
////        Intent createShortcutIconIntent = new Intent(INTENT_LAUNCH_SHORTCUT);
////        // 标题
////        createShortcutIconIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
////        // 图标
////        createShortcutIconIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
////        // Intent
////        createShortcutIconIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
////        // 发送广播创建图标
////        sendBroadcast(createShortcutIconIntent);
//
//        try {
//            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, "appName")
//                    .setShortLabel("ShortLabel")
//                    .setLongLabel("LongLabel")
//                    .setIcon(Icon.createWithBitmap(icon))
//                    .setIntent(intent)
//                    .build();
//            shortcutManager.requestPinShortcut(shortcutInfo, null);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//}