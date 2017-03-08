package com.myntra.coachmarks.builder;

import android.graphics.Point;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class InfoForViewToMask implements Parcelable {

    public static Builder create(Point viewToMaskStartPosition, int viewToMaskHeight, int viewToMaskWidth) {
        return new AutoValue_InfoForViewToMask.Builder()
                .setViewToMaskStartPosition(viewToMaskStartPosition)
                .setViewToMaskHeight(viewToMaskHeight)
                .setViewToMaskWidth(viewToMaskWidth);
    }

    public abstract Point getViewToMaskStartPosition();

    public abstract int getViewToMaskHeight();

    public abstract int getViewToMaskWidth();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setViewToMaskStartPosition(Point viewToMaskStartPosition);

        public abstract Builder setViewToMaskHeight(int viewToMaskHeight);

        public abstract Builder setViewToMaskWidth(int viewToMaskWidth);

        public abstract InfoForViewToMask build();

    }

}
