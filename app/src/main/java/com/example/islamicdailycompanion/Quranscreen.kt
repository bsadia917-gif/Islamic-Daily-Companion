
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuranScreen(
    onSurahClick: (String) -> Unit,
    onTrackerClick: () -> Unit
) {

    val context = LocalContext.current

    val surahs = remember {
        QuranRepository.loadQuran(context)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredSurahs = remember(
        searchQuery,
        surahs
    ) {

        if (searchQuery.isBlank()) {

            surahs

        } else {

            surahs.filter { surah ->

                surah.transliteration.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||
                        surah.name.contains(
                            searchQuery
                        ) ||
                        surah.number.toString() ==
                        searchQuery.trim()
            }
        }
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
            .padding(16.dp)
    ) {

        // =====================================
        // QURAN TITLE
        // =====================================

        Text(
            text = "📖 Quran",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        Text(
            text = "Read the Holy Quran",
            fontSize = 15.sp,
            color = Color.DarkGray
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =====================================
        // QURAN TRACKER BUTTON
        // =====================================

        Button(
            onClick = {
                onTrackerClick()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {

            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = "Quran Tracker"
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = "Quran Tracker",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        // =====================================
        // SEARCH
        // =====================================

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            leadingIcon = {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            placeholder = {

                Text(
                    text = "Search Surah..."
                )
            }
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // =====================================
        // SURAH COUNT
        // =====================================

        Text(
            text = if (searchQuery.isBlank()) {
                "114 Surahs"
            } else {
                "${filteredSurahs.size} Surahs found"
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // =====================================
        // SURAH LIST
        // =====================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            items(
                items = filteredSurahs
            ) { surah ->

                Card(
                    onClick = {
                        onSurahClick(surah.name)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
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
                            .padding(15.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // =====================================
                        // SURAH NUMBER
                        // =====================================

                        Card(
                            modifier = Modifier.size(48.dp),
                            shape =
                                RoundedCornerShape(14.dp),
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        Color(0xFFE8F5E9)
                                )
                        ) {

                            Box(
                                modifier =
                                    Modifier.fillMaxSize(),
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text =
                                        surah.number.toString(),
                                    fontSize = 17.sp,
                                    color =
                                        Color(0xFF2E7D32),
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.size(14.dp)
                        )

                        // =====================================
                        // SURAH INFORMATION
                        // =====================================

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    surah.transliteration,
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.Medium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text = surah.name,
                                fontSize = 22.sp,
                                color =
                                    Color(0xFF2E7D32),
                                textAlign =
                                    TextAlign.Right,
                                modifier =
                                    Modifier.fillMaxWidth()
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    "${surah.type} • ${surah.totalVerses} Ayahs",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        // =====================================
                        // ARROW
                        // =====================================

                        Text(
                            text = "›",
                            fontSize = 30.sp,
                            color =
                                Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
}

