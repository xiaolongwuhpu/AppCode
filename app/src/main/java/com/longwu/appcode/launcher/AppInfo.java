package com.longwu.appcode.launcher;
import android.graphics.drawable.Drawable;

public class AppInfo {
    private String name;
    private String packageName;
    private String className;
    private Drawable icon;

    public AppInfo(String name, String packageName, String className, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public Drawable getIcon() {
        return icon;
    }
}
