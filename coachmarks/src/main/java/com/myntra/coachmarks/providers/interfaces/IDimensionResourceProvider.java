package com.myntra.coachmarks.providers.interfaces;

import android.support.annotation.DimenRes;

public interface IDimensionResourceProvider {
    int getDimensionInPixel(@DimenRes final int dimenRes);
}
