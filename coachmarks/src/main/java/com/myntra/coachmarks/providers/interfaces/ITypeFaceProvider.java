package com.myntra.coachmarks.providers.interfaces;

import android.graphics.Typeface;
import android.support.annotation.Nullable;

public interface ITypeFaceProvider {
    @Nullable
    Typeface getTypeFaceFromAssets(final String fontFilePath);
}
