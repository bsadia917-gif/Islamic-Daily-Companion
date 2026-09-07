package com.example.islamicdailycompanion

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object QuranTrackerManager {

    private const val PREF_NAME = "quran_tracker"

    private const val KEY_LAST_SURAH = "last_surah"
    private const val KEY_LAST_PAGE = "last_page"
    private const val KEY_DAILY_GOAL = "daily_goal"

    private const val DEFAULT_DAILY_GOAL = 5

    private fun getPreferences(context: Context) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun getDateKey(
        calendar: Calendar = Calendar.getInstance()
    ): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(calendar.time)
    }

    fun getTodayRuku(context: Context): Int {
        val prefs = getPreferences(context)
        val key = "ruku_${getDateKey()}"
        return prefs.getInt(key, 0)
    }

    fun addRuku(
        context: Context,
        amount: Int = 1
    ) {
        val prefs = getPreferences(context)
        val key = "ruku_${getDateKey()}"

        val currentRuku = prefs.getInt(key, 0)
        val dailyGoal = getDailyGoal(context)

        // 5 par pohanchne ke baad aur add nahi hoga
        val newRuku = (currentRuku + amount)
            .coerceAtMost(dailyGoal)

        prefs.edit()
            .putInt(key, newRuku)
            .apply()
    }

    fun removeRuku(
        context: Context,
        amount: Int = 1
    ) {
        val prefs = getPreferences(context)
        val key = "ruku_${getDateKey()}"

        val currentRuku = prefs.getInt(key, 0)

        // 0 se neeche nahi jayega
        val newRuku = (currentRuku - amount)
            .coerceAtLeast(0)

        prefs.edit()
            .putInt(key, newRuku)
            .apply()
    }

    fun resetTodayRuku(context: Context) {
        val prefs = getPreferences(context)
        val key = "ruku_${getDateKey()}"

        prefs.edit()
            .putInt(key, 0)
            .apply()
    }

    fun getDailyGoal(context: Context): Int {
        return getPreferences(context)
            .getInt(
                KEY_DAILY_GOAL,
                DEFAULT_DAILY_GOAL
            )
    }

    fun setDailyGoal(
        context: Context,
        goal: Int
    ) {
        if (goal <= 0) return

        getPreferences(context)
            .edit()
            .putInt(KEY_DAILY_GOAL, goal)
            .apply()
    }

    fun saveLastRead(
        context: Context,
        surahName: String,
        page: Int
    ) {
        getPreferences(context)
            .edit()
            .putString(KEY_LAST_SURAH, surahName)
            .putInt(KEY_LAST_PAGE, page)
            .apply()
    }

    fun getLastSurah(context: Context): String {
        return getPreferences(context)
            .getString(
                KEY_LAST_SURAH,
                "Not started yet"
            )
            ?: "Not started yet"
    }

    fun getLastPage(context: Context): Int {
        return getPreferences(context)
            .getInt(KEY_LAST_PAGE, 0)
    }

    fun getReadingStreak(context: Context): Int {

        val prefs = getPreferences(context)

        val today = Calendar.getInstance()

        val startDate = Calendar.getInstance()

        val todayKey =
            "ruku_${getDateKey(today)}"

        // Agar aaj abhi kuch nahi padha,
        // to kal se streak check hogi.
        if (prefs.getInt(todayKey, 0) <= 0) {
            startDate.add(
                Calendar.DAY_OF_YEAR,
                -1
            )
        }

        var streak = 0

        while (true) {

            val key =
                "ruku_${getDateKey(startDate)}"

            val ruku =
                prefs.getInt(key, 0)

            if (ruku <= 0) {
                break
            }

            streak++

            startDate.add(
                Calendar.DAY_OF_YEAR,
                -1
            )
        }

        return streak
    }
}