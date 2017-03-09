package com.myntra.coachmarks.builder;

import android.os.Parcelable;
import android.support.annotation.DimenRes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ImageLayoutInformation implements Parcelable {

    public static Builder create(@DimenRes int imageWidth, @DimenRes int imageHeight) {
        return new AutoValue_ImageLayoutInformation.Builder()
                .setImageWidth(imageWidth)
                .setImageHeight(imageHeight);
    }

    @DimenRes
    public abstract int getImageHeight();

    @DimenRes
    public abstract int getImageWidth();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setImageHeight(@DimenRes int imageHeight);

        public abstract Builder setImageWidth(@DimenRes int imageWidth);

        public abstract ImageLayoutInformation build();
    }
}
