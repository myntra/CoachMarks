package com.myntra.coachmarks.providers;

import android.content.Context;
import android.support.annotation.DimenRes;

import com.myntra.coachmarks.providers.interfaces.IDimensionResourceProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DefaultDimensionResourceProvider implements IDimensionResourceProvider {

    private final Context mContext;

    public DefaultDimensionResourceProvider(final Context context) {
        mContext = context;
    }

    @Override
    public int getDimensionInPixel(@DimenRes final int dimenRes) {
        return mContext.getResources().getDimensionPixelSize(dimenRes);
    }
}
