
package com.example.islamicdailycompanion

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


// =========================================
// PRAYER HISTORY SCREEN
// =========================================

@Composable
fun PrayerHistoryScreen() {

    val context = LocalContext.current

    val preferences = remember {

        context.getSharedPreferences(
            "prayer_history",
            Context.MODE_PRIVATE
        )
    }


    // =====================================
    // TODAY DATE
    // =====================================

    val today = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())


    // =====================================
    // CURRENT MONTH
    // =====================================

    val currentMonth = SimpleDateFormat(
        "yyyy-MM",
        Locale.getDefault()
    ).format(Date())


    // =====================================
    // TODAY'S PRAYERS
    // =====================================

    var fajrToday by remember {
        mutableStateOf(false)
    }

    var dhuhrToday by remember {
        mutableStateOf(false)
    }

    var asrToday by remember {
        mutableStateOf(false)
    }

    var maghribToday by remember {
        mutableStateOf(false)
    }

    var ishaToday by remember {
        mutableStateOf(false)
    }


    // =====================================
    // MONTHLY COUNTS
    // =====================================

    var fajrMonth by remember {
        mutableStateOf(0)
    }

    var dhuhrMonth by remember {
        mutableStateOf(0)
    }

    var asrMonth by remember {
        mutableStateOf(0)
    }

    var maghribMonth by remember {
        mutableStateOf(0)
    }

    var ishaMonth by remember {
        mutableStateOf(0)
    }


    // =====================================
    // LOAD DATA
    // =====================================

    LaunchedEffect(Unit) {

        // Today's status

        fajrToday =
            preferences.getBoolean(
                "${today}_Fajr",
                false
            )

        dhuhrToday =
            preferences.getBoolean(
                "${today}_Dhuhr",
                false
            )

        asrToday =
            preferences.getBoolean(
                "${today}_Asr",
                false
            )

        maghribToday =
            preferences.getBoolean(
                "${today}_Maghrib",
                false
            )

        ishaToday =
            preferences.getBoolean(
                "${today}_Isha",
                false
            )


        // Monthly totals

        fajrMonth =
            getMonthlyPrayerCount(
                preferences,
                currentMonth,
                "Fajr"
            )

        dhuhrMonth =
            getMonthlyPrayerCount(
                preferences,
                currentMonth,
                "Dhuhr"
            )

        asrMonth =
            getMonthlyPrayerCount(
                preferences,
                currentMonth,
                "Asr"
            )

        maghribMonth =
            getMonthlyPrayerCount(
                preferences,
                currentMonth,
                "Maghrib"
            )

        ishaMonth =
            getMonthlyPrayerCount(
                preferences,
                currentMonth,
                "Isha"
            )
    }


    // =====================================
    // TODAY TOTAL
    // =====================================

    val todayTotal =
        listOf(
            fajrToday,
            dhuhrToday,
            asrToday,
            maghribToday,
            ishaToday
        ).count { it }


    // =====================================
    // MONTH TOTAL
    // =====================================

    val monthTotal =
        fajrMonth +
                dhuhrMonth +
                asrMonth +
                maghribMonth +
                ishaMonth


    // =====================================
    // UI
    // =====================================

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE8F5E9),
                        Color.White
                    )
                )
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {


        // =================================
        // TITLE
        // =================================

        Text(
            text = "🕌 Prayer History",
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Your Salah Record",
            fontSize = 17.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // =================================
        // TODAY
        // =================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = androidx.compose.foundation.shape
                .RoundedCornerShape(20.dp),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Today's Prayer",
                    fontSize = 21.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = today,
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "$todayTotal / 5 Prayers Completed",
                    fontSize = 22.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(18.dp)
        )


        // =================================
        // TODAY'S PRAYERS
        // =================================

        PrayerHistoryCard(
            prayerName = "Fajr",
            isCompleted = fajrToday
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        PrayerHistoryCard(
            prayerName = "Dhuhr",
            isCompleted = dhuhrToday
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        PrayerHistoryCard(
            prayerName = "Asr",
            isCompleted = asrToday
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        PrayerHistoryCard(
            prayerName = "Maghrib",
            isCompleted = maghribToday
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        PrayerHistoryCard(
            prayerName = "Isha",
            isCompleted = ishaToday
        )


        Spacer(
            modifier = Modifier.height(25.dp)
        )


        // =================================
        // MONTHLY HISTORY
        // =================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape =
                androidx.compose.foundation.shape
                    .RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "📅 This Month",
                    fontSize = 23.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Monthly Salah Record",
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )


                PrayerMonthlyRow(
                    prayerName = "Fajr",
                    count = fajrMonth
                )

                PrayerMonthlyRow(
                    prayerName = "Dhuhr",
                    count = dhuhrMonth
                )

                PrayerMonthlyRow(
                    prayerName = "Asr",
                    count = asrMonth
                )

                PrayerMonthlyRow(
                    prayerName = "Maghrib",
                    count = maghribMonth
                )

                PrayerMonthlyRow(
                    prayerName = "Isha",
                    count = ishaMonth
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Total Salah",
                        fontSize = 18.sp
                    )

                    Text(
                        text = "$monthTotal",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}


// =========================================
// TODAY PRAYER CARD
// =========================================

@Composable
fun PrayerHistoryCard(
    prayerName: String,
    isCompleted: Boolean
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape =
            androidx.compose.foundation.shape
                .RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isCompleted) {
                        Color(0xFFE8F5E9)
                    } else {
                        Color(0xFFFFEBEE)
                    }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = prayerName,
                fontSize = 20.sp
            )

            Text(
                text =
                    if (isCompleted) {
                        "✅ Read"
                    } else {
                        "❌ Not Read"
                    },

                fontSize = 17.sp
            )
        }
    }
}


// =========================================
// MONTHLY ROW
// =========================================

@Composable
fun PrayerMonthlyRow(
    prayerName: String,
    count: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = prayerName,
            fontSize = 17.sp
        )

        Text(
            text = "$count times",
            fontSize = 17.sp
        )
    }
}


// =========================================
// MONTHLY COUNT FUNCTION
// =========================================

fun getMonthlyPrayerCount(
    preferences: android.content.SharedPreferences,
    month: String,
    prayerName: String
): Int {

    var count = 0

    val allData =
        preferences.all

    for (key in allData.keys) {

        if (
            key.startsWith("${month}-") &&
            key.endsWith("_$prayerName")
        ) {

            val value =
                preferences.getBoolean(
                    key,
                    false
                )

            if (value) {
                count++
            }
        }
    }

    return count
}

