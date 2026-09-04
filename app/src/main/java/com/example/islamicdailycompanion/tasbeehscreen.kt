package com.example.islamicdailycompanion

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
fun TasbeehScreen() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "tasbeeh_data",
            Context.MODE_PRIVATE
        )
    }

    // Today's date
    val today = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    ).format(Date())

    // Selected Dhikr
    var selectedDhikr by remember {

        mutableStateOf(
            preferences.getString(
                "selected_dhikr",
                "SubhanAllah"
            ) ?: "SubhanAllah"
        )
    }

    // Current session count
    var count by remember {

        mutableStateOf(
            preferences.getInt(
                "${today}_${selectedDhikr}_session",
                0
            )
        )
    }

    // Today's total count
    var todayTotal by remember {

        mutableStateOf(
            preferences.getInt(
                "${today}_${selectedDhikr}_total",
                0
            )
        )
    }

    // Target
    var target by remember {

        mutableStateOf(
            preferences.getInt(
                "target",
                100
            )
        )
    }

    // --------------------------------
    // Select Dhikr
    // --------------------------------

    fun selectDhikr(dhikr: String) {

        selectedDhikr = dhikr

        count = preferences.getInt(
            "${today}_${dhikr}_session",
            0
        )

        todayTotal = preferences.getInt(
            "${today}_${dhikr}_total",
            0
        )

        preferences.edit()
            .putString(
                "selected_dhikr",
                dhikr
            )
            .apply()
    }

    // --------------------------------
    // Add One Tasbeeh
    // --------------------------------

    fun addOne() {

        // Current session always increases
        count++

        // Today's total also increases
        todayTotal++

        // Save date for history
        val savedDates =
            preferences.getStringSet(
                "history_dates",
                emptySet()
            )?.toMutableSet()
                ?: mutableSetOf()

        savedDates.add(today)

        preferences.edit()

            // Current session
            .putInt(
                "${today}_${selectedDhikr}_session",
                count
            )

            // Today's total
            .putInt(
                "${today}_${selectedDhikr}_total",
                todayTotal
            )

            // Save date
            .putStringSet(
                "history_dates",
                savedDates
            )

            .apply()
    }

    // --------------------------------
    // Progress
    // --------------------------------

    val progress = if (target > 0) {

        (count.toFloat() / target.toFloat())
            .coerceIn(0f, 1f)

    } else {

        0f
    }

    // --------------------------------
    // UI
    // --------------------------------

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
            .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        // --------------------------------
        // Title
        // --------------------------------

        Text(
            text = "📿 Digital Tasbeeh",
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Remember Allah",
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // --------------------------------
        // Selected Dhikr
        // --------------------------------

        Card(

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(20.dp)
        ) {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Selected Dhikr",
                    fontSize = 15.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = selectedDhikr,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // --------------------------------
        // TODAY TOTAL
        // --------------------------------

        Card(

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(20.dp)
        ) {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Today's $selectedDhikr",
                    fontSize = 17.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "$todayTotal times",
                    fontSize = 28.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        // --------------------------------
        // DHIKR BUTTONS
        // --------------------------------

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(

                onClick = {
                    selectDhikr("SubhanAllah")
                },

                modifier = Modifier.weight(1f)
            ) {

                Text("SubhanAllah")
            }

            OutlinedButton(

                onClick = {
                    selectDhikr("Alhamdulillah")
                },

                modifier = Modifier.weight(1f)
            ) {

                Text("Alhamdulillah")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(

                onClick = {
                    selectDhikr("Allahu Akbar")
                },

                modifier = Modifier.weight(1f)
            ) {

                Text("Allahu Akbar")
            }

            OutlinedButton(

                onClick = {
                    selectDhikr("Durood Shareef")
                },

                modifier = Modifier.weight(1f)
            ) {

                Text("Durood Shareef")
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // --------------------------------
        // CURRENT SESSION COUNTER
        // --------------------------------

        Box(

            modifier = Modifier

                .size(220.dp)

                .background(
                    color = Color(0xFFE0F2F1),
                    shape = CircleShape
                )

                .clickable {
                    addOne()
                },

            contentAlignment =
                Alignment.Center
        ) {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "$count",
                    fontSize = 55.sp
                )

                Text(
                    text = "/ $target",
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "TAP",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // --------------------------------
        // PROGRESS
        // --------------------------------

        LinearProgressIndicator(

            progress = {
                progress
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "$count / $target completed"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // --------------------------------
        // TARGET
        // --------------------------------

        Text(
            text = "Choose Target",
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Row(

            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Button(

                onClick = {

                    target = 100

                    preferences.edit()
                        .putInt(
                            "target",
                            100
                        )
                        .apply()
                }
            ) {

                Text("100")
            }

            Button(

                onClick = {

                    target = 200

                    preferences.edit()
                        .putInt(
                            "target",
                            200
                        )
                        .apply()
                }
            ) {

                Text("200")
            }
        }

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        // --------------------------------
        // RESET SESSION
        // --------------------------------

        OutlinedButton(

            onClick = {

                count = 0

                preferences.edit()
                    .putInt(
                        "${today}_${selectedDhikr}_session",
                        0
                    )
                    .apply()
            }
        ) {

            Text("🔄 Reset Session")
        }
    }
}