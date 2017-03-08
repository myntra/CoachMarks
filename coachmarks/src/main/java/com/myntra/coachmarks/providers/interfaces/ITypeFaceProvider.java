package com.myntra.coachmarks.providers.interfaces;

import android.graphics.Typeface;

public interface ITypeFaceProvider {
    Typeface getTypeFaceFromAssets(final String fontFilePath);
}
