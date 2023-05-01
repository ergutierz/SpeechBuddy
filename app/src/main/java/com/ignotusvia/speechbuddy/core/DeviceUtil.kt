package com.ignotusvia.speechbuddy.core

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator

val Activity.isTabletDevice: Boolean
    get() {
    val metrics = WindowMetricsCalculator.getOrCreate()
        .computeCurrentWindowMetrics(this)

    val widthDp = metrics.bounds.width() /
            resources.displayMetrics.density
    return widthDp > MIN_TABLET_SIZE
}

private const val MIN_TABLET_SIZE: Float = 700f