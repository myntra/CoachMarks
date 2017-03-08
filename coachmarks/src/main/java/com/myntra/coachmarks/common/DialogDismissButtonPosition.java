package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({DialogDismissButtonPosition.LEFT,
        DialogDismissButtonPosition.RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface DialogDismissButtonPosition {
    int LEFT = 0;
    int RIGHT = 1;
}