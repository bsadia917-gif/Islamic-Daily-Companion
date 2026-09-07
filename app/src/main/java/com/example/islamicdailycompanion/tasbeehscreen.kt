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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import java.util.Calendar
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

    // =====================================
    // TODAY
    // =====================================

    val today = SimpleDateFormat(
        "dd-MM-yyyy",
        Locale.getDefault()
    ).format(Date())

    // =====================================
    // REMOVE HISTORY OLDER THAN 5 DAYS
    // =====================================

    remember {

        val savedDates =
            preferences.getStringSet(
                "history_dates",
                emptySet()
            )?.toMutableSet()
                ?: mutableSetOf()

        val validDates = mutableSetOf<String>()

        val dateFormat =
            SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            )

        for (i in 0 until 5) {

            val calendar =
                Calendar.getInstance()

            calendar.add(
                Calendar.DAY_OF_YEAR,
                -i
            )

            validDates.add(
                dateFormat.format(
                    calendar.time
                )
            )
        }

        val editor = preferences.edit()

        savedDates.forEach { date ->

            if (!validDates.contains(date)) {

                val dhikrList = listOf(
                    "SubhanAllah",
                    "Alhamdulillah",
                    "Allahu Akbar",
                    "Durood Shareef",
                    "Astaghfirullah"
                )

                dhikrList.forEach { dhikr ->

                    editor.remove(
                        "${date}_${dhikr}_total"
                    )

                    editor.remove(
                        "${date}_${dhikr}_session"
                    )
                }
            }
        }

        editor
            .putStringSet(
                "history_dates",
                savedDates
                    .filter {
                        validDates.contains(it)
                    }
                    .toSet()
            )
            .apply()
    }

    // =====================================
    // SELECTED DHIKR
    // =====================================

    var selectedDhikr by remember {

        mutableStateOf(
            preferences.getString(
                "selected_dhikr",
                "SubhanAllah"
            ) ?: "SubhanAllah"
        )
    }

    // =====================================
    // CURRENT SESSION
    // =====================================

    var count by remember {

        mutableStateOf(
            preferences.getInt(
                "${today}_${selectedDhikr}_session",
                0
            )
        )
    }

    // =====================================
    // TODAY TOTAL
    // =====================================

    var todayTotal by remember {

        mutableStateOf(
            preferences.getInt(
                "${today}_${selectedDhikr}_total",
                0
            )
        )
    }

    // =====================================
    // TARGET
    // =====================================

    var target by remember {

        mutableStateOf(
            preferences.getInt(
                "target",
                100
            )
        )
    }

    // =====================================
    // MANUAL DIALOG
    // =====================================

    var showManualDialog by remember {
        mutableStateOf(false)
    }

    var manualCountText by remember {
        mutableStateOf("")
    }

    // =====================================
    // SELECT DHIKR
    // =====================================

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

    // =====================================
    // SAVE HISTORY DATE
    // =====================================

    fun saveTodayInHistory() {

        val savedDates =
            preferences.getStringSet(
                "history_dates",
                emptySet()
            )?.toMutableSet()
                ?: mutableSetOf()

        savedDates.add(today)

        preferences.edit()
            .putStringSet(
                "history_dates",
                savedDates
            )
            .apply()
    }

    // =====================================
    // ADD ONE
    // =====================================

    fun addOne() {

        count++
        todayTotal++

        preferences.edit()
            .putInt(
                "${today}_${selectedDhikr}_session",
                count
            )
            .putInt(
                "${today}_${selectedDhikr}_total",
                todayTotal
            )
            .apply()

        saveTodayInHistory()
    }

    // =====================================
    // ADD MANUAL COUNT
    // =====================================

    fun addManualCount(amount: Int) {

        if (amount <= 0) return

        count += amount
        todayTotal += amount

        preferences.edit()
            .putInt(
                "${today}_${selectedDhikr}_session",
                count
            )
            .putInt(
                "${today}_${selectedDhikr}_total",
                todayTotal
            )
            .apply()

        saveTodayInHistory()
    }

    // =====================================
    // PROGRESS
    // =====================================

    val progress =
        if (target > 0) {

            (count.toFloat() / target.toFloat())
                .coerceIn(0f, 1f)

        } else {
            0f
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

        // =====================================
        // SELECTED DHIKR
        // =====================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

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
                    Modifier
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
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text = selectedDhikr,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        // =====================================
        // TODAY TOTAL
        // =====================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(20.dp)
        ) {

            Column(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "Today's $selectedDhikr",

                    fontSize =
                        17.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        "$todayTotal times",

                    fontSize =
                        28.sp
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        // =====================================
        // DHIKR BUTTONS ROW 1
        // =====================================

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(

                onClick = {
                    selectDhikr("SubhanAllah")
                },

                modifier =
                    Modifier.weight(1f)
            ) {

                Text("SubhanAllah")
            }

            OutlinedButton(

                onClick = {
                    selectDhikr("Alhamdulillah")
                },

                modifier =
                    Modifier.weight(1f)
            ) {

                Text("Alhamdulillah")
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // =====================================
        // DHIKR BUTTONS ROW 2
        // =====================================

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            OutlinedButton(

                onClick = {
                    selectDhikr("Allahu Akbar")
                },

                modifier =
                    Modifier.weight(1f)
            ) {

                Text("Allahu Akbar")
            }

            OutlinedButton(

                onClick = {
                    selectDhikr("Durood Shareef")
                },

                modifier =
                    Modifier.weight(1f)
            ) {

                Text("Durood Shareef")
            }
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // =====================================
        // ASTAGHFIRULLAH
        // =====================================

        OutlinedButton(

            onClick = {
                selectDhikr("Astaghfirullah")
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Astaghfirullah")
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        // =====================================
        // CURRENT SESSION COUNTER
        // =====================================

        Box(

            modifier =
                Modifier
                    .size(220.dp)
                    .background(
                        color =
                            Color(0xFFE0F2F1),
                        shape =
                            CircleShape
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
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text = "TAP",
                    fontSize = 14.sp
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        // =====================================
        // PROGRESS
        // =====================================

        LinearProgressIndicator(

            progress = {
                progress
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "$count / $target completed"
        )

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        // =====================================
        // MANUAL ADD
        // =====================================

        OutlinedButton(

            onClick = {

                manualCountText = ""

                showManualDialog = true
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("➕ Add Manual Count")
        }

        Spacer(
            modifier =
                Modifier.height(15.dp)
        )

        // =====================================
        // TARGET
        // =====================================

        Text(
            text = "Choose Target",
            fontSize = 18.sp
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
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
            modifier =
                Modifier.height(15.dp)
        )

        // =====================================
        // RESET
        // =====================================

        OutlinedButton(

            onClick = {

                count = 0

                preferences.edit()
                    .putInt(
                        "${today}_${selectedDhikr}_session",
                        0
                    )
                    .apply()
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("🔄 Reset Current Session")
        }

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )
    }

    // =====================================
    // MANUAL COUNT DIALOG
    // =====================================

    if (showManualDialog) {

        AlertDialog(

            onDismissRequest = {
                showManualDialog = false
            },

            title = {

                Text(
                    text =
                        "Add Manual Count"
                )
            },

            text = {

                Column {

                    Text(
                        text =
                            "Add the number of $selectedDhikr you already read."
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    OutlinedTextField(

                        value =
                            manualCountText,

                        onValueChange = { value ->

                            if (
                                value.all {
                                    it.isDigit()
                                }
                            ) {

                                manualCountText =
                                    value
                            }
                        },

                        label = {
                            Text("Count")
                        },

                        singleLine = true
                    )
                }
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        val amount =
                            manualCountText
                                .toIntOrNull()

                        if (
                            amount != null &&
                            amount > 0
                        ) {

                            addManualCount(
                                amount
                            )

                            showManualDialog =
                                false
                        }
                    }
                ) {

                    Text("Add")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showManualDialog = false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }
}