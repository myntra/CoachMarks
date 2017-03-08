package com.myntra.coachmarks.ui.common;

import android.support.annotation.CallSuper;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseViews {

    private View mRoot;

    protected BaseViews(View root) {
        mRoot = root;
        ButterKnife.bind(this, root);
    }

    public View getRootView() {
        return mRoot;
    }

    @CallSuper
    public void clear() {
        mRoot = null;
    }

}
