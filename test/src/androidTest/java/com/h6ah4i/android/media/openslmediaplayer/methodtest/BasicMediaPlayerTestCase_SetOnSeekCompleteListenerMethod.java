/*
 *    Copyright (C) 2014 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */


package com.h6ah4i.android.media.openslmediaplayer.methodtest;

import junit.framework.TestSuite;

import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.openslmediaplayer.base.BasicMediaPlayerStateTestCaseBase;
import com.h6ah4i.android.media.openslmediaplayer.base.BasicMediaPlayerTestCaseBase;
import com.h6ah4i.android.media.openslmediaplayer.utils.BasicMediaPlayerEventListenerObject;
import com.h6ah4i.android.media.openslmediaplayer.utils.CompletionListenerObject;
import com.h6ah4i.android.media.openslmediaplayer.utils.ErrorListenerObject;
import com.h6ah4i.android.media.openslmediaplayer.utils.SeekCompleteListenerObject;
import com.h6ah4i.android.media.openslmediaplayer.testing.ParameterizedTestArgs;

public class BasicMediaPlayerTestCase_SetOnSeekCompleteListenerMethod
        extends BasicMediaPlayerStateTestCaseBase {

    public static TestSuite buildTestSuite(Class<? extends IMediaPlayerFactory> factoryClazz) {
        return BasicMediaPlayerTestCaseBase.buildBasicTestSuite(
                BasicMediaPlayerTestCase_SetOnSeekCompleteListenerMethod.class, factoryClazz);
    }

    public BasicMediaPlayerTestCase_SetOnSeekCompleteListenerMethod(ParameterizedTestArgs args) {
        super(args);
    }

    private void expectsNoErrorsWithSeek(IBasicMediaPlayer player, int msec) {
        Object sharedSyncObj = new Object();
        ErrorListenerObject err = new ErrorListenerObject(sharedSyncObj, false);
        CompletionListenerObject comp = new CompletionListenerObject(sharedSyncObj);
        SeekCompleteListenerObject seek = new SeekCompleteListenerObject(sharedSyncObj);

        player.setOnErrorListener(err);
        player.setOnCompletionListener(comp);
        player.setOnSeekCompleteListener(seek);

        player.seekTo(msec);

        if (!BasicMediaPlayerEventListenerObject.awaitAny(
                DEFAULT_EVENT_WAIT_DURATION, err, comp, seek)) {
            fail();
        }

        player.setOnErrorListener(null);
        player.setOnCompletionListener(null);
        player.setOnSeekCompleteListener(null);

        assertFalse(comp.occurred());
        assertFalse(err.occurred());
        assertTrue(seek.occurred());
    }

    private void expectsNoErrorsWithNoSeek(IBasicMediaPlayer player) {
        Object sharedSyncObj = new Object();
        ErrorListenerObject err = new ErrorListenerObject(sharedSyncObj, false);
        CompletionListenerObject comp = new CompletionListenerObject(sharedSyncObj);
        SeekCompleteListenerObject seek = new SeekCompleteListenerObject(sharedSyncObj);

        player.setOnErrorListener(err);
        player.setOnCompletionListener(comp);
        player.setOnSeekCompleteListener(seek);

        if (BasicMediaPlayerEventListenerObject.awaitAny(
                SHORT_EVENT_WAIT_DURATION, err, comp, seek)) {
            fail();
        }

        player.setOnErrorListener(null);
        player.setOnCompletionListener(null);
        player.setOnSeekCompleteListener(null);

        assertFalse(comp.occurred());
        assertFalse(err.occurred());
        assertFalse(seek.occurred());
    }

    private static int getSeekPosition(IBasicMediaPlayer player) {
        return (safeGetDuration(player, DURATION_LIMIT)) / 2;
    }

    @Override
    protected void onTestStateIdle(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStateInitialized(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStatePreparing(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStatePrepared(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithSeek(player, getSeekPosition(player));
    }

    @Override
    protected void onTestStateStarted(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithSeek(player, getSeekPosition(player));
    }

    @Override
    protected void onTestStatePaused(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithSeek(player, getSeekPosition(player));
    }

    @Override
    protected void onTestStateStopped(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStatePlaybackCompleted(IBasicMediaPlayer player, Object args)
            throws Throwable {
        expectsNoErrorsWithSeek(player, getSeekPosition(player));
    }

    @Override
    protected void onTestStateErrorBeforePrepared(IBasicMediaPlayer player, Object args)
            throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStateErrorAfterPrepared(IBasicMediaPlayer player, Object args)
            throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }

    @Override
    protected void onTestStateEnd(IBasicMediaPlayer player, Object args) throws Throwable {
        expectsNoErrorsWithNoSeek(player);
    }
}
