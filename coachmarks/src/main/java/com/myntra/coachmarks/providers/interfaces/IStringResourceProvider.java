package com.myntra.coachmarks.providers.interfaces;

import android.support.annotation.StringRes;

public interface IStringResourceProvider {

    String getStringFromResource(@StringRes final int stringRes);

    String getStringFromResource(@StringRes final int stringResId,
                                 final Object... formatArgs);

}