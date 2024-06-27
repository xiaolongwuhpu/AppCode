package com.longwu.appcode.util

import android.app.PendingIntent
import android.os.Build

object TargetSSupport {

    fun pendingIntentFlagImmutable(flags: Int): Int {
        return flags or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
    }

    fun pendingIntentFlagMutable(flags: Int): Int {
        return flags or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
    }
}