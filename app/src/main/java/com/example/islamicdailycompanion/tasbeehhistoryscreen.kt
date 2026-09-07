
package com.example.islamicdailycompanion

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
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

    // =====================================
    // DATE FORMAT
    // =====================================

    val dateFormat = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    )

    val today = dateFormat.format(Date())

    // =====================================
    // LAST 5 DAYS
    // =====================================

    val historyDates = remember {

        val dates = mutableListOf<String>()

        for (i in 0 until 5) {

            val calendar = Calendar.getInstance()

            calendar.add(
                Calendar.DAY_OF_YEAR,
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

    // =====================================
    // DHIKR NAMES
    // =====================================

    val duroodName = "Durood Shareef"
    val subhanAllahName = "SubhanAllah"
    val alhamdulillahName = "Alhamdulillah"
    val allahuAkbarName = "Allahu Akbar"
    val astaghfirullahName = "Astaghfirullah"

    // =====================================
    // GET COUNT
    // =====================================

    fun getCount(
        date: String,
        dhikr: String
    ): Int {

        return preferences.getInt(
            "${date}_${dhikr}_total",
            0
        )
    }

    // =====================================
    // 5-DAY TOTALS
    // =====================================

    val totalDurood = historyDates.sumOf { date ->

        getCount(
            date,
            duroodName
        )
    }

    val totalSubhanAllah = historyDates.sumOf { date ->

        getCount(
            date,
            subhanAllahName
        )
    }

    val totalAlhamdulillah = historyDates.sumOf { date ->

        getCount(
            date,
            alhamdulillahName
        )
    }

    val totalAllahuAkbar = historyDates.sumOf { date ->

        getCount(
            date,
            allahuAkbarName
        )
    }

    val totalAstaghfirullah = historyDates.sumOf { date ->

        getCount(
            date,
            astaghfirullahName
        )
    }

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
            .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        // =====================================
        // TITLE
        // =====================================

        Text(
            text = "📊 Tasbeeh History",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Last 5 Days",
            fontSize = 17.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =====================================
        // TODAY CARD
        // =====================================

        val todayTotal =
            getCount(today, duroodName) +
                    getCount(today, subhanAllahName) +
                    getCount(today, alhamdulillahName) +
                    getCount(today, allahuAkbarName) +
                    getCount(today, astaghfirullahName)

        Card(

            modifier =
                Modifier.width(180.dp),

            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(16.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Today",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = today,
                    fontSize = 14.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text = "$todayTotal times",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // =====================================
        // DAILY RECORD TITLE
        // =====================================

        Text(
            text = "📅 Daily Record",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        // =====================================
        // HORIZONTAL SCROLL TABLE
        // =====================================

        Row(

            modifier =
                Modifier
                    .fillMaxSize()
                    .horizontalScroll(
                        rememberScrollState()
                    )
        ) {

            Column {

                // =================================
                // HEADER ROW
                // =================================

                Row(

                    modifier =
                        Modifier.background(
                            Color(0xFF2E7D32),
                            RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp
                            )
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    HistoryHeaderCell(
                        text = "Date"
                    )

                    HistoryHeaderCell(
                        text = "Durood\nShareef"
                    )

                    HistoryHeaderCell(
                        text = "Subhan\nAllah"
                    )

                    HistoryHeaderCell(
                        text = "Alhamdu\nlillah"
                    )

                    HistoryHeaderCell(
                        text = "Allahu\nAkbar"
                    )

                    HistoryHeaderCell(
                        text = "Astaghfi\nrullah"
                    )
                }

                // =================================
                // 5 DAYS
                // =================================

                historyDates.forEach { date ->

                    val durood =
                        getCount(
                            date,
                            duroodName
                        )

                    val subhanAllah =
                        getCount(
                            date,
                            subhanAllahName
                        )

                    val alhamdulillah =
                        getCount(
                            date,
                            alhamdulillahName
                        )

                    val allahuAkbar =
                        getCount(
                            date,
                            allahuAkbarName
                        )

                    val astaghfirullah =
                        getCount(
                            date,
                            astaghfirullahName
                        )

                    HistoryDataRow(

                        date = date,

                        durood =
                            durood,

                        subhanAllah =
                            subhanAllah,

                        alhamdulillah =
                            alhamdulillah,

                        allahuAkbar =
                            allahuAkbar,

                        astaghfirullah =
                            astaghfirullah
                    )
                }

                // =================================
                // TOTAL ROW
                // =================================

                HistoryTotalRow(

                    totalDurood =
                        totalDurood,

                    totalSubhanAllah =
                        totalSubhanAllah,

                    totalAlhamdulillah =
                        totalAlhamdulillah,

                    totalAllahuAkbar =
                        totalAllahuAkbar,

                    totalAstaghfirullah =
                        totalAstaghfirullah
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        Text(
            text =
                "Total = separate total for each Dhikr",

            fontSize =
                13.sp,

            color =
                Color.Gray
        )
    }
}


// =====================================
// HEADER CELL
// =====================================

@Composable
fun HistoryHeaderCell(
    text: String
) {

    Text(

        text = text,

        modifier =
            Modifier
                .width(110.dp)
                .padding(
                    vertical = 12.dp,
                    horizontal = 5.dp
                ),

        color =
            Color.White,

        fontSize =
            13.sp,

        fontWeight =
            FontWeight.Bold,

        textAlign =
            TextAlign.Center
    )
}


// =====================================
// DATA ROW
// =====================================

@Composable
fun HistoryDataRow(

    date: String,

    durood: Int,

    subhanAllah: Int,

    alhamdulillah: Int,

    allahuAkbar: Int,

    astaghfirullah: Int
) {

    Row(

        modifier =
            Modifier.background(
                Color.White
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        HistoryDataCell(
            text = date
        )

        HistoryDataCell(
            text =
                durood.toString()
        )

        HistoryDataCell(
            text =
                subhanAllah.toString()
        )

        HistoryDataCell(
            text =
                alhamdulillah.toString()
        )

        HistoryDataCell(
            text =
                allahuAkbar.toString()
        )

        HistoryDataCell(
            text =
                astaghfirullah.toString()
        )
    }
}


// =====================================
// TOTAL ROW
// =====================================

@Composable
fun HistoryTotalRow(

    totalDurood: Int,

    totalSubhanAllah: Int,

    totalAlhamdulillah: Int,

    totalAllahuAkbar: Int,

    totalAstaghfirullah: Int
) {

    Row(

        modifier =
            Modifier.background(
                Color(0xFFC8E6C9),
                RoundedCornerShape(
                    bottomStart = 14.dp,
                    bottomEnd = 14.dp
                )
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // =================================
        // TOTAL UNDER DATE
        // =================================

        HistoryTotalCell(
            text = "TOTAL"
        )

        // =================================
        // DUROOD TOTAL
        // =================================

        HistoryTotalCell(
            text =
                totalDurood.toString()
        )

        // =================================
        // SUBHANALLAH TOTAL
        // =================================

        HistoryTotalCell(
            text =
                totalSubhanAllah.toString()
        )

        // =================================
        // ALHAMDULILLAH TOTAL
        // =================================

        HistoryTotalCell(
            text =
                totalAlhamdulillah.toString()
        )

        // =================================
        // ALLAHU AKBAR TOTAL
        // =================================

        HistoryTotalCell(
            text =
                totalAllahuAkbar.toString()
        )

        // =================================
        // ASTAGHFIRULLAH TOTAL
        // =================================

        HistoryTotalCell(
            text =
                totalAstaghfirullah.toString()
        )
    }
}


// =====================================
// DATA CELL
// =====================================

@Composable
fun HistoryDataCell(
    text: String
) {

    Text(

        text = text,

        modifier =
            Modifier
                .width(110.dp)
                .padding(
                    vertical = 12.dp,
                    horizontal = 5.dp
                ),

        fontSize =
            14.sp,

        textAlign =
            TextAlign.Center
    )
}


// =====================================
// TOTAL CELL
// =====================================

@Composable
fun HistoryTotalCell(
    text: String
) {

    Text(

        text = text,

        modifier =
            Modifier
                .width(110.dp)
                .padding(
                    vertical = 14.dp,
                    horizontal = 5.dp
                ),

        fontSize =
            15.sp,

        fontWeight =
            FontWeight.Bold,

        color =
            Color(0xFF1B5E20),

        textAlign =
            TextAlign.Center
    )
}

