package com.myntra.coachmarks.ui.common;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseViews {

    @Nullable
    private View mRoot;

    protected BaseViews(@NonNull View root) {
        mRoot = root;
        ButterKnife.bind(this, root);
    }

    @Nullable
    public View getRootView() {
        return mRoot;
    }

    @CallSuper
    public void clear() {
        mRoot = null;
    }

}
