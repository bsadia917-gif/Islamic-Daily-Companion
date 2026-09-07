package com.example.islamicdailycompanion

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val QuranGreen = Color(0xFF2E7D32)
private val LightGreen = Color(0xFFE8F5E9)
private val DarkGreen = Color(0xFF1B5E20)

@Composable
fun QuranTrackerScreen(
    onContinueReading: () -> Unit,
    onAllSurahs: () -> Unit
) {

    val context = LocalContext.current

    val dailyGoal =
        QuranTrackerManager.getDailyGoal(context)

    var todayRuku by remember {
        mutableStateOf(
            QuranTrackerManager.getTodayRuku(context)
        )
    }

    val lastSurah =
        QuranTrackerManager.getLastSurah(context)

    val lastPage =
        QuranTrackerManager.getLastPage(context)

    val streak =
        QuranTrackerManager.getReadingStreak(context)

    val progress =
        (todayRuku.toFloat() / dailyGoal.toFloat())
            .coerceIn(0f, 1f)

    val percentage =
        (progress * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF7))
            .padding(20.dp)
    ) {

        Text(
            text = "Quran Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGreen
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Track your daily Quran reading",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Today's Progress",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222222)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightGreen
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.size(180.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator(
                        progress = {
                            progress
                        },
                        modifier = Modifier.size(170.dp),
                        color = QuranGreen,
                        trackColor =
                            Color(0xFFC8E6C9),
                        strokeWidth = 14.dp
                    )

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "$percentage%",
                            fontSize = 32.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color = DarkGreen
                        )

                        Text(
                            text =
                                "$todayRuku / $dailyGoal Ruku",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text =
                        "Today's Goal: $dailyGoal Ruku",
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Medium,
                    color = DarkGreen
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ADD / REMOVE BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Button(
                        onClick = {

                            QuranTrackerManager.addRuku(
                                context,
                                1
                            )

                            todayRuku =
                                QuranTrackerManager
                                    .getTodayRuku(context)
                        },
                        modifier =
                            Modifier.weight(1f),
                        enabled =
                            todayRuku < dailyGoal,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    QuranGreen,
                                disabledContainerColor =
                                    Color(0xFF9E9E9E)
                            )
                    ) {

                        Text(
                            text = "+ Add Ruku"
                        )
                    }

                    Button(
                        onClick = {

                            QuranTrackerManager
                                .removeRuku(
                                    context,
                                    1
                                )

                            todayRuku =
                                QuranTrackerManager
                                    .getTodayRuku(context)
                        },
                        modifier =
                            Modifier.weight(1f),
                        enabled =
                            todayRuku > 0,
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFC62828),
                                disabledContainerColor =
                                    Color(0xFF9E9E9E)
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Remove,
                            contentDescription =
                                "Remove Ruku"
                        )

                        Spacer(
                            modifier =
                                Modifier.size(4.dp)
                        )

                        Text(
                            text = "Remove"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                if (todayRuku >= dailyGoal) {

                    Text(
                        text =
                            "🎉 Daily goal completed!",
                        fontSize = 14.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = DarkGreen
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // LAST READ
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = QuranGreen,
                    modifier = Modifier.size(38.dp)
                )

                Spacer(
                    modifier = Modifier.size(14.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Last Read",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = lastSurah,
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = DarkGreen
                    )

                    if (lastPage > 0) {

                        Text(
                            text = "Page $lastPage",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                    } else {

                        Text(
                            text =
                                "No page recorded yet",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        // STREAK
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    Color(0xFFFFF3E0)
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "🔥",
                    fontSize = 32.sp
                )

                Spacer(
                    modifier = Modifier.size(14.dp)
                )

                Column {

                    Text(
                        text = "Reading Streak",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "$streak Days",
                        fontSize = 21.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFFE65100)
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        // CONTINUE + ALL SURAHS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = onContinueReading,
                modifier =
                    Modifier.weight(1f),
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            QuranGreen
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.PlayArrow,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = "Continue"
                )
            }

            Button(
                onClick = onAllSurahs,
                modifier =
                    Modifier.weight(1f),
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            DarkGreen
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.School,
                    contentDescription = null
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = "All Surahs"
                )
            }
        }
    }
}