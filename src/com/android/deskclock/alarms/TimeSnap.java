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

/**
 * Pure helpers for turning a scrubber position into a snapped time of day.
 * No Android dependencies, so it can be unit tested on the JVM.
 */
public final class TimeSnap {

    public static final int MINUTES_PER_DAY = 24 * 60;
    public static final int DEFAULT_STEP_MINUTES = 15;

    private TimeSnap() {}

    /**
     * Maps a scrubber position to a minute of the day, snapped to {@code stepMinutes}.
     *
     * @param fraction position along the scrubber, clamped to [0, 1]
     * @param startMinute minute of day at fraction 0
     * @param endMinute minute of day at fraction 1 (must be greater than startMinute)
     * @param stepMinutes snap interval in minutes (must be positive)
     * @return minute of day in [0, 1440), wrapping 24:00 to 00:00
     */
    public static int minuteOfDay(float fraction, int startMinute, int endMinute,
            int stepMinutes) {
        if (stepMinutes <= 0) {
            throw new IllegalArgumentException("stepMinutes must be positive");
        }
        if (endMinute <= startMinute) {
            throw new IllegalArgumentException("endMinute must be greater than startMinute");
        }
        final float clamped = Math.max(0f, Math.min(1f, fraction));
        final float raw = startMinute + clamped * (endMinute - startMinute);
        final int snapped = Math.round(raw / stepMinutes) * stepMinutes;
        return ((snapped % MINUTES_PER_DAY) + MINUTES_PER_DAY) % MINUTES_PER_DAY;
    }

    /** Whole-day scrubber with the default 15 minute step. */
    public static int minuteOfDay(float fraction) {
        return minuteOfDay(fraction, 0, MINUTES_PER_DAY, DEFAULT_STEP_MINUTES);
    }

    public static int hourOf(int minuteOfDay) {
        return minuteOfDay / 60;
    }

    public static int minuteOf(int minuteOfDay) {
        return minuteOfDay % 60;
    }
}
