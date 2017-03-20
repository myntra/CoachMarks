package com.myntra.coachmarks.providers;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.myntra.coachmarks.providers.interfaces.IScreenInfoProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultScreenInfoProviderTest {

    @Mock
    Context mMockContext;
    @Mock
    Resources mMockedResources;

    IScreenInfoProvider mScreenInfoProvider;
    DisplayMetrics mDisplayMetrics;


    @Before
    public void setUp() throws Exception {
        mDisplayMetrics = new DisplayMetrics();
        mDisplayMetrics.heightPixels = 1780;
        mDisplayMetrics.widthPixels = 1024;

        mScreenInfoProvider = new DefaultScreenInfoProvider(mMockContext);
    }

    @Test
    public void getScreenHeightInPixelsTest() throws Exception {
        when(mMockContext.getResources()).thenReturn(mMockedResources);
        when(mMockedResources.getDisplayMetrics()).thenReturn(mDisplayMetrics);
        assert (mScreenInfoProvider.getScreenHeightInPixels() == mDisplayMetrics.heightPixels);
        verify(mMockContext, times(1)).getResources();
        verify(mMockedResources, times(1)).getDisplayMetrics();
        verifyNoMoreInteractions(mMockContext);
        verifyNoMoreInteractions(mMockedResources);
    }

    @Test
    public void getScreenWidthInPixelsTest() throws Exception {
        when(mMockContext.getResources()).thenReturn(mMockedResources);
        when(mMockedResources.getDisplayMetrics()).thenReturn(mDisplayMetrics);
        assert (mScreenInfoProvider.getScreenWidthInPixels() == mDisplayMetrics.widthPixels);
        verify(mMockContext, times(1)).getResources();
        verify(mMockedResources, times(1)).getDisplayMetrics();
        verifyNoMoreInteractions(mMockContext);
        verifyNoMoreInteractions(mMockedResources);
    }

}