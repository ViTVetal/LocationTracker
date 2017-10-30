package com.gocodes.locationtracker.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by vit-vetal- on 29.10.17.
 */

public class SizeConverter {
    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
