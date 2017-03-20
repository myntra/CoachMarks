package com.myntra.coachmarks.providers;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;

import com.myntra.coachmarks.providers.interfaces.IDimensionResourceProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDimensionResourceProviderTest {

    @DimenRes
    private static final int MOCKED_DIMEN_RES = 1;
    private static final int MOCKED_PIXEL_DIMEN = 200;
    @Mock
    Context mMockContext;
    @Mock
    Resources mMockedResources;

    IDimensionResourceProvider mDimensionResourceProvider;

    @Before
    public void setUp() {
        mDimensionResourceProvider = new DefaultDimensionResourceProvider(mMockContext);
    }

    @Test
    public void getDimensionInPixelTest() throws Exception {
        when(mMockContext.getResources()).thenReturn(mMockedResources);
        when(mMockedResources.getDimensionPixelSize(MOCKED_DIMEN_RES)).thenReturn(MOCKED_PIXEL_DIMEN);
        assert (mDimensionResourceProvider.getDimensionInPixel(MOCKED_DIMEN_RES) == MOCKED_PIXEL_DIMEN);
        verify(mMockContext, times(1)).getResources();
        verify(mMockedResources, times(1)).getDimensionPixelSize(anyInt());
        verifyNoMoreInteractions(mMockContext);
        verifyNoMoreInteractions(mMockedResources);
    }

    @Test
    public void getDimensionInPixelFailTest() throws Exception {
        when(mMockContext.getResources()).thenReturn(mMockedResources);
        when(mMockedResources.getDimensionPixelSize(MOCKED_DIMEN_RES)).thenReturn(MOCKED_PIXEL_DIMEN);
        assert (mDimensionResourceProvider.getDimensionInPixel(MOCKED_DIMEN_RES) != 100);
        verify(mMockContext, times(1)).getResources();
        verify(mMockedResources, times(1)).getDimensionPixelSize(anyInt());
        verifyNoMoreInteractions(mMockContext);
        verifyNoMoreInteractions(mMockedResources);
    }

}