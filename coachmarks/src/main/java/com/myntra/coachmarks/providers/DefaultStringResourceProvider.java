package com.myntra.coachmarks.providers;

import android.content.Context;
import android.support.annotation.StringRes;

import com.myntra.coachmarks.providers.interfaces.IStringResourceProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DefaultStringResourceProvider implements IStringResourceProvider {

    private final Context mContext;

    public DefaultStringResourceProvider(final Context context) {
        mContext = context;
    }

    @Override
    public String getStringFromResource(@StringRes final int stringRes) {
        return mContext.getString(stringRes);
    }

    @Override
    public String getStringFromResource(@StringRes final int stringResId, final Object... formatArgs) {
        return mContext.getString(stringResId, formatArgs);
    }
}
