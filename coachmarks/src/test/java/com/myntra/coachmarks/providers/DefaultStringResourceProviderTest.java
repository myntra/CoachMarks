package com.myntra.coachmarks.providers;

import android.content.Context;
import android.support.annotation.StringRes;

import com.myntra.coachmarks.providers.interfaces.IStringResourceProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStringResourceProviderTest {

    private static final String MOCKED_STRING = "This is a mocked string";
    private static final String MOCK_TEST = "mocked";
    @StringRes
    private static final int MOCKED_STRING_RES = 1;

    @Mock
    Context mMockContext;

    IStringResourceProvider mStringResourceProvider;

    @Before
    public void setUp() throws Exception {
        mStringResourceProvider = new DefaultStringResourceProvider(mMockContext);
    }

    @Test
    public void getStringFromResourceTest() throws Exception {
        when(mMockContext.getString(MOCKED_STRING_RES)).thenReturn(MOCKED_STRING);
        assert (mStringResourceProvider.getStringFromResource(MOCKED_STRING_RES).equals(MOCKED_STRING));
        verify(mMockContext, times(1)).getString(anyInt());
        verifyNoMoreInteractions(mMockContext);
    }

    @Test
    public void getStringFromResourceWithArgsTest() throws Exception {
        when(mMockContext.getString(MOCKED_STRING_RES, MOCK_TEST)).thenReturn(MOCKED_STRING);
        assert (mStringResourceProvider.getStringFromResource(MOCKED_STRING_RES, MOCK_TEST).equals(MOCKED_STRING));
        verify(mMockContext, times(1)).getString(anyInt(), Matchers.any());
        verifyNoMoreInteractions(mMockContext);
    }

}