# Ideas

Working notes for this fork. Each idea is meant to be built as a small,
atomic change (one widget or behaviour at a time).

## Context

Most people wake at the same time every morning (e.g. a 9 to 5 schedule), so
the alarm UI should make the usual alarm one touch away.

## 1. Most-recently-used alarm list

- Today the list is ordered by time of day (`DEFAULT_SORT_ORDER` in
  `src/com/android/deskclock/provider/Alarm.java`).
- Proposed: order by last use, so the alarm you actually use sits at the top.
- Everything lives on the first screen.
- "Use" means created, edited or toggled on.
- Needs: a `last_used` column on `alarm_templates` (DB migration), updated on
  those actions, and the sort order changed to `last_used DESC`.
- Open: pin enabled alarms above disabled ones?

## 2. Scrub-to-set time picker

- A horizontal scrubber showing the time, dragged left and right.
- Snaps to 15-minute steps. The time is shown on the thumb.
- Pops up from the bottom of the alarm list, as part of the list flow, like
  the menus that appear from the top but from the opposite direction.
  Reached by scrolling past the bottom of the list.
- This slider is the main way to set a time.
- Replaces the stock `TimePickerDialog` (`alarms/TimePickerDialogFragment.java`).
- Open: whole day, or a window (e.g. 04:00 to 10:00)? New alarm or edit the
  top one? Fine-tuning for times that are not on a quarter hour.

## 3. Wake-up sound

- Gradual volume rise (DeskClock already has a native crescendo setting).
- Any audio file as the alarm sound (the picker already uses
  `ACTION_OPEN_DOCUMENT` with a persistable URI permission).

## 4. Reliability and usability

- Alarms that silently turn off, snooze mistakes and forgotten alarms are the
  main concerns, more than basic scheduling.
- Keep the existing state machine (`AlarmStateManager`) and missed-alarm state.
