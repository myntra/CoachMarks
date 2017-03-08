package com.myntra.coachmarks.providers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.myntra.coachmarks.providers.interfaces.ITypeFaceProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DefaultTypeFaceProvider implements ITypeFaceProvider {

    private Context mContext;

    public DefaultTypeFaceProvider(final Context context) {
        mContext = context;
    }

    @Override
    public Typeface getTypeFaceFromAssets(final String fontFilePath) {
        try {
            return Typeface.createFromAsset(mContext.getAssets(), fontFilePath);
        } catch (RuntimeException e) {
            Log.e(DefaultTypeFaceProvider.class.getName(), e.getMessage());
        }
        return null;
    }
}
