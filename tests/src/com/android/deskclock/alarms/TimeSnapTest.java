/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.deskclock.alarms;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeSnapTest {

    @Test
    public void startAndEndOfDay() {
        assertEquals(0, TimeSnap.minuteOfDay(0f));
        // 24:00 wraps to 00:00
        assertEquals(0, TimeSnap.minuteOfDay(1f));
    }

    @Test
    public void middleOfDayIsNoon() {
        assertEquals(12 * 60, TimeSnap.minuteOfDay(0.5f));
    }

    @Test
    public void snapsToNearestQuarterHour() {
        // 7:07 is nearer 7:00, 7:08 is nearer 7:15
        final float perMinute = 1f / TimeSnap.MINUTES_PER_DAY;
        assertEquals(7 * 60, TimeSnap.minuteOfDay((7 * 60 + 7) * perMinute));
        assertEquals(7 * 60 + 15, TimeSnap.minuteOfDay((7 * 60 + 8) * perMinute));
    }

    @Test
    public void clampsOutOfRangeFractions() {
        assertEquals(0, TimeSnap.minuteOfDay(-3f));
        assertEquals(0, TimeSnap.minuteOfDay(5f));
    }

    @Test
    public void windowedScrubber() {
        // 04:00 to 10:00 window
        assertEquals(4 * 60, TimeSnap.minuteOfDay(0f, 4 * 60, 10 * 60, 15));
        assertEquals(7 * 60, TimeSnap.minuteOfDay(0.5f, 4 * 60, 10 * 60, 15));
        assertEquals(10 * 60, TimeSnap.minuteOfDay(1f, 4 * 60, 10 * 60, 15));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveStep() {
        TimeSnap.minuteOfDay(0.5f, 0, 1440, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsEmptyWindow() {
        TimeSnap.minuteOfDay(0.5f, 600, 600, 15);
    }

    @Test
    public void hourAndMinuteSplit() {
        assertEquals(7, TimeSnap.hourOf(7 * 60 + 45));
        assertEquals(45, TimeSnap.minuteOf(7 * 60 + 45));
    }
}
