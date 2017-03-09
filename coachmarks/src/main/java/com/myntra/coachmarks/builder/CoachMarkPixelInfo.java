package com.myntra.coachmarks.builder;

import android.graphics.Rect;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CoachMarkPixelInfo implements Parcelable {

    public static CoachMarkPixelInfo.Builder create() {
        return new AutoValue_CoachMarkPixelInfo.Builder()
                .setImageWidthInPixels(0)
                .setImageHeightInPixels(0)
                .setMarginRectInPixels(new Rect(0, 0, 0, 0))
                .setPopUpWidthInPixelsWithOffset(0)
                .setPopUpHeightInPixelsWithOffset(0)
                .setPopUpWidthInPixels(0)
                .setPopUpHeightInPixels(0)
                .setScreenWidthInPixels(0)
                .setScreenHeightInPixels(0)
                .setNotchDimenInPixels(0)
                .setActionBarHeightPixels(0)
                .setFooterHeightPixels(0)
                .setMarginOffsetForNotchInPixels(0)
                .setWidthHeightOffsetForCoachMarkPopUp(0);
    }

    public abstract int getImageWidthInPixels();

    public abstract int getImageHeightInPixels();

    public abstract Rect getMarginRectInPixels();

    public abstract int getPopUpWidthInPixelsWithOffset();

    public abstract int getPopUpHeightInPixelsWithOffset();

    public abstract int getPopUpWidthInPixels();

    public abstract int getPopUpHeightInPixels();

    public abstract int getScreenWidthInPixels();

    public abstract int getScreenHeightInPixels();

    public abstract int getNotchDimenInPixels();

    public abstract int getActionBarHeightPixels();

    public abstract int getFooterHeightPixels();

    public abstract int getMarginOffsetForNotchInPixels();

    public abstract int getWidthHeightOffsetForCoachMarkPopUp();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setImageWidthInPixels(int imageWidthInPixels);

        public abstract Builder setImageHeightInPixels(int imageHeightInPixels);

        public abstract Builder setMarginRectInPixels(Rect coachMarkMarginRectInPixels);

        public abstract Builder setPopUpWidthInPixelsWithOffset(int coachMarkPopUpWidthInPixelsWithOffset);

        public abstract Builder setPopUpHeightInPixelsWithOffset(int coachMarkPopUpHeightInPixelsWithOffset);

        public abstract Builder setPopUpWidthInPixels(int coachMarkPopUpWidthInPixels);

        public abstract Builder setPopUpHeightInPixels(int coachMarkPopUpHeightInPixels);

        public abstract Builder setScreenWidthInPixels(int screenWidthInPixels);

        public abstract Builder setScreenHeightInPixels(int screenHeightInPixels);

        public abstract Builder setNotchDimenInPixels(int notchDimenInPixels);

        public abstract Builder setActionBarHeightPixels(int actionBarHeightPixels);

        public abstract Builder setFooterHeightPixels(int footerHeightPixels);

        public abstract Builder setMarginOffsetForNotchInPixels(int marginOffsetForNotchInPixels);

        public abstract Builder setWidthHeightOffsetForCoachMarkPopUp(int widthHeightOffsetForCoachMarkPopUp);

        public abstract CoachMarkPixelInfo build();

    }
}
