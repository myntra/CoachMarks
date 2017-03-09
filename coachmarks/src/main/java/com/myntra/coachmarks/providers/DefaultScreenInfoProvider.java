package com.myntra.coachmarks.providers;

import android.content.Context;
import android.util.DisplayMetrics;

import com.myntra.coachmarks.providers.interfaces.IScreenInfoProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DefaultScreenInfoProvider implements IScreenInfoProvider {

    private final Context mContext;

    public DefaultScreenInfoProvider(final Context context) {
        mContext = context;
    }

    @Override
    public int getScreenHeightInPixels() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    @Override
    public int getScreenWidthInPixels() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
