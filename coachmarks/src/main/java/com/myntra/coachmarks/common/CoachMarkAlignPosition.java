package com.myntra.coachmarks.common;

import android.support.annotation.IntDef;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({CoachMarkAlignPosition.ALIGN_LEFT,
        CoachMarkAlignPosition.ALIGN_TOP,
        CoachMarkAlignPosition.ALIGN_RIGHT,
        CoachMarkAlignPosition.ALIGN_BOTTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface CoachMarkAlignPosition {
    int ALIGN_LEFT = RelativeLayout.ALIGN_LEFT;
    int ALIGN_TOP = RelativeLayout.ALIGN_TOP;
    int ALIGN_RIGHT = RelativeLayout.ALIGN_RIGHT;
    int ALIGN_BOTTOM = RelativeLayout.ALIGN_BOTTOM;
}