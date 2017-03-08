package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({CoachMarkLayoutOrientation.HORIZONTAL, CoachMarkLayoutOrientation.VERTICAL})
@Retention(RetentionPolicy.SOURCE)
public @interface CoachMarkLayoutOrientation {
    int HORIZONTAL = LinearLayout.HORIZONTAL;
    int VERTICAL = LinearLayout.VERTICAL;
}