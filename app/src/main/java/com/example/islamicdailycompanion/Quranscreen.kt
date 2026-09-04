package com.example.islamicdailycompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuranScreen(
    onSurahClick: (String) -> Unit
) {

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
            .padding(16.dp)
    ) {

        // Header
        Text(
            text = "📖 Quran",
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Read the Holy Quran",
            fontSize = 15.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // Quran Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2E7D32)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "🕌",
                    fontSize = 40.sp
                )

                Spacer(
                    modifier = Modifier.size(14.dp)
                )

                Column {

                    Text(
                        text = "Holy Quran",
                        fontSize = 22.sp,
                        color = Color.White
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Select a Surah to read",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Surahs",
            fontSize = 22.sp
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // Surah List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            itemsIndexed(
                surahList
            ) { index, surah ->

                Card(
                    onClick = {
                        onSurahClick(surah.name)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // Surah Number
                        Card(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color(0xFFE8F5E9)
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier.fillMaxSize(),
                                horizontalAlignment =
                                    Alignment.CenterHorizontally,
                                verticalArrangement =
                                    Arrangement.Center
                            ) {

                                Text(
                                    text = "${index + 1}",
                                    fontSize = 17.sp,
                                    color =
                                        Color(0xFF2E7D32)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.size(14.dp)
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = surah.name,
                                fontSize = 19.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text = "Tap to read",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 30.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}