package com.myntra.coachmarks.providers;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;

import com.myntra.coachmarks.providers.interfaces.ITypeFaceProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Typeface.class, Log.class})
public class DefaultTypeFaceProviderTest {

    private static final String ASSET_PATH = "defaultPath";

    @Mock
    Context mMockContext;
    @Mock
    Resources mMockedResources;
    @Mock
    Typeface mMockedTypeFace;
    @Mock
    AssetManager mMockedAssetManager;

    ITypeFaceProvider mTypeFaceProvider;

    @Before
    public void setUp() throws Exception {
        mTypeFaceProvider = new DefaultTypeFaceProvider(mMockContext);
    }

    @Test
    public void getTypeFaceFromAssetsTest() throws Exception {
        PowerMockito.mockStatic(Typeface.class);
        when(mMockContext.getAssets()).thenReturn(mMockedAssetManager);
        when(Typeface.createFromAsset(mMockedAssetManager, ASSET_PATH)).thenReturn(mMockedTypeFace);

        Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(ASSET_PATH);
        PowerMockito.verifyStatic(times(1));
        assert (typeface == mMockedTypeFace);
        PowerMockito.verifyNoMoreInteractions();
    }

    @Test
    public void getTypeFaceFromAssetsFailTest() throws Exception {
        PowerMockito.mockStatic(Typeface.class);
        PowerMockito.mockStatic(Log.class);
        when(mMockContext.getAssets()).thenReturn(mMockedAssetManager);
        when(Typeface.createFromAsset(mMockedAssetManager, ASSET_PATH)).thenThrow(new RuntimeException());
        when(Log.e(anyString(), anyString())).thenReturn(1);

        Typeface typeface = mTypeFaceProvider.getTypeFaceFromAssets(ASSET_PATH);
        PowerMockito.verifyStatic(times(2));
        assert (typeface == null);
        PowerMockito.verifyNoMoreInteractions();
    }


}