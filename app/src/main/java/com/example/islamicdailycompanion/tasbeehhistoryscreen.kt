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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.util.Date
import java.util.Locale

@Composable
fun TasbeehHistoryScreen() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "tasbeeh_data",
            Context.MODE_PRIVATE
        )
    }

    val today = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    ).format(Date())

    // Today's counts
    var subhanAllah by remember {
        mutableStateOf(
            preferences.getInt(
                "${today}_SubhanAllah_total",
                0
            )
        )
    }

    var alhamdulillah by remember {
        mutableStateOf(
            preferences.getInt(
                "${today}_Alhamdulillah_total",
                0
            )
        )
    }

    var allahuAkbar by remember {
        mutableStateOf(
            preferences.getInt(
                "${today}_Allahu Akbar_total",
                0
            )
        )
    }

    var duroodShareef by remember {
        mutableStateOf(
            preferences.getInt(
                "${today}_Durood Shareef_total",
                0
            )
        )
    }

    val totalToday =
        subhanAllah +
                alhamdulillah +
                allahuAkbar +
                duroodShareef

    /*
     * =====================================
     * HISTORY DATA
     * =====================================
     *
     * Hum last 30 days check kar rahe hain.
     *
     * Har date ka:
     * SubhanAllah
     * Alhamdulillah
     * Allahu Akbar
     * Durood Shareef
     *
     * separately save hai.
     */

    val dateFormat = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    )

    val historyDates = remember {

        val dates = mutableListOf<String>()

        for (i in 0 until 30) {

            val calendar =
                java.util.Calendar.getInstance()

            calendar.add(
                java.util.Calendar.DAY_OF_YEAR,
                -i
            )

            dates.add(
                dateFormat.format(
                    calendar.time
                )
            )
        }

        dates
    }

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

        // =====================================
        // TITLE
        // =====================================

        Text(
            text = "📊 Tasbeeh History",
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Your Dhikr Record",
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =====================================
        // TODAY
        // =====================================

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Today",
                    fontSize = 20.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = today,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        // =====================================
        // TODAY TOTAL
        // =====================================

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Total Dhikr Today",
                    fontSize = 19.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "$totalToday times",
                    fontSize = 32.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =====================================
        // TODAY'S SEPARATE COUNTERS
        // =====================================

        HistoryCard(
            title = "SubhanAllah",
            count = subhanAllah
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        HistoryCard(
            title = "Alhamdulillah",
            count = alhamdulillah
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        HistoryCard(
            title = "Allahu Akbar",
            count = allahuAkbar
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        HistoryCard(
            title = "Durood Shareef",
            count = duroodShareef
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =====================================
        // PREVIOUS DAYS
        // =====================================

        Text(
            text = "📅 Previous Days",
            fontSize = 22.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        historyDates.forEach { date ->

            val subhan =
                preferences.getInt(
                    "${date}_SubhanAllah_total",
                    0
                )

            val alhamdulillahCount =
                preferences.getInt(
                    "${date}_Alhamdulillah_total",
                    0
                )

            val allahuAkbarCount =
                preferences.getInt(
                    "${date}_Allahu Akbar_total",
                    0
                )

            val durood =
                preferences.getInt(
                    "${date}_Durood Shareef_total",
                    0
                )

            val dayTotal =
                subhan +
                        alhamdulillahCount +
                        allahuAkbarCount +
                        durood

            /*
             * Empty days show nahi karenge.
             */
            if (dayTotal > 0) {

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        Text(
                            text = "📅 $date",
                            fontSize = 19.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        HistoryRow(
                            title = "SubhanAllah",
                            count = subhan
                        )

                        HistoryRow(
                            title = "Alhamdulillah",
                            count =
                                alhamdulillahCount
                        )

                        HistoryRow(
                            title = "Allahu Akbar",
                            count =
                                allahuAkbarCount
                        )

                        HistoryRow(
                            title = "Durood Shareef",
                            count = durood
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        HistoryRow(
                            title = "Total",
                            count = dayTotal
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}


// =====================================
// HISTORY CARD
// =====================================

@Composable
fun HistoryCard(
    title: String,
    count: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = title,
                fontSize = 19.sp
            )

            Text(
                text = "$count times",
                fontSize = 25.sp
            )
        }
    }
}


// =====================================
// HISTORY ROW
// =====================================

@Composable
fun HistoryRow(
    title: String,
    count: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            fontSize = 16.sp
        )

        Text(
            text = "$count times",
            fontSize = 16.sp
        )
    }
}


