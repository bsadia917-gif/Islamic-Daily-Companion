package com.example.islamicdailycompanion

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onQuranClick: () -> Unit,
    onDuaClick: () -> Unit,
    onTasbeehClick: () -> Unit,
    onPrayerClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "user_profile",
            Context.MODE_PRIVATE
        )
    }

    var profileBitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    // =====================================
    // LOAD SAVED PROFILE IMAGE
    // =====================================

    LaunchedEffect(Unit) {

        val imageUriString =
            preferences.getString(
                "profile_image_uri",
                null
            )

        if (!imageUriString.isNullOrEmpty()) {

            try {

                val uri =
                    Uri.parse(imageUriString)

                val inputStream =
                    context.contentResolver
                        .openInputStream(uri)

                profileBitmap =
                    BitmapFactory
                        .decodeStream(inputStream)

                inputStream?.close()

            } catch (
                e: Exception
            ) {

                profileBitmap = null
            }
        }
    }

    // =====================================
    // USER NAME
    // =====================================

    val userName =
        preferences.getString(
            "user_name",
            ""
        ) ?: ""

    val displayName =
        if (userName.isNotBlank()) {
            userName
        } else {
            "Welcome"
        }

    // =====================================
    // DATE
    // =====================================

    val today =
        SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            Locale.getDefault()
        ).format(Date())

    // =====================================
    // UI
    // =====================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF7FBF7)
            )
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // =================================
            // ATTRACTIVE HEADER
            // =================================

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1B5E20),
                                    Color(0xFF388E3C),
                                    Color(0xFF66BB6A)
                                )
                            ),
                            RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            )
                        )
                        .padding(
                            start = 20.dp,
                            end = 12.dp,
                            top = 25.dp,
                            bottom = 25.dp
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = 55.dp
                            )
                    ) {

                        Text(
                            text = "🌙  Assalamu Alaikum",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(7.dp)
                        )

                        Text(
                            text =
                                if (
                                    displayName == "Welcome"
                                ) {
                                    "Welcome to your Islamic Daily Companion"
                                } else {
                                    "Welcome, $displayName"
                                },
                            color = Color.White,
                            fontSize = 16.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(10.dp)
                        )

                        Text(
                            text = today,
                            color =
                                Color.White.copy(
                                    alpha = 0.9f
                                ),
                            fontSize = 14.sp
                        )
                    }

                    // =================================
                    // PROFILE BUTTON
                    // =================================

                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(52.dp)
                            .background(
                                Color.White.copy(
                                    alpha = 0.20f
                                ),
                                CircleShape
                            )
                    ) {

                        if (profileBitmap != null) {

                            Image(
                                bitmap =
                                    profileBitmap!!
                                        .asImageBitmap(),

                                contentDescription =
                                    "Profile",

                                modifier =
                                    Modifier
                                        .size(46.dp)
                                        .clip(
                                            CircleShape
                                        ),

                                contentScale =
                                    ContentScale.Crop
                            )

                        } else {

                            Icon(
                                imageVector =
                                    Icons.Default.Person,

                                contentDescription =
                                    "Profile",

                                tint =
                                    Color.White,

                                modifier =
                                    Modifier.size(
                                        32.dp
                                    )
                            )
                        }
                    }
                }
            }

            // =================================
            // DAILY REMINDER
            // =================================

            item {

                Card(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFF2E7D32)
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                    ) {

                        Text(
                            text =
                                "🌿 Daily Reminder",

                            color =
                                Color.White,

                            fontSize =
                                20.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Remember Allah and keep your heart connected with Him.",

                            color =
                                Color.White.copy(
                                    alpha = 0.95f
                                ),

                            fontSize =
                                15.sp
                        )
                    }
                }
            }

            // =================================
            // EXPLORE
            // =================================

            item {

                Text(
                    text = "Explore",

                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp
                        ),

                    fontSize = 23.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color(0xFF16351A)
                )
            }

            // =================================
            // QURAN
            // =================================

            item {

                HomeFeatureCard(
                    icon = "📖",
                    title = "Quran",
                    description =
                        "Read Surahs and verses",
                    onClick = onQuranClick
                )
            }

            // =================================
            // DUAS
            // =================================

            item {

                HomeFeatureCard(
                    icon = "🤲",
                    title = "Daily Duas",
                    description =
                        "Read and remember daily duas",
                    onClick = onDuaClick
                )
            }

            // =================================
            // TASBEEH
            // =================================

            item {

                HomeFeatureCard(
                    icon = "📿",
                    title = "Digital Tasbeeh",
                    description =
                        "Count your Dhikr easily",
                    onClick = onTasbeehClick
                )
            }

            // =================================
            // PRAYER
            // =================================

            item {

                HomeFeatureCard(
                    icon = "🕌",
                    title = "Prayer Times",
                    description =
                        "Check today's prayer times",
                    onClick = onPrayerClick
                )
            }

            // =================================
            // FOOTER
            // =================================

            item {

                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 8.dp,
                                bottom = 25.dp
                            ),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            "🕌 Islamic Daily Companion",

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Medium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "May Allah bless your day 🤍",

                        fontSize =
                            13.sp,

                        color =
                            Color.Gray
                    )
                }
            }
        }
    }
}


// =====================================
// HOME FEATURE CARD
// =====================================

@Composable
fun HomeFeatureCard(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(20.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(17.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier =
                    Modifier
                        .size(58.dp)
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(17.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = icon,
                    fontSize = 30.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.width(15.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = title,

                    fontSize = 19.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text = description,

                    fontSize = 14.sp,

                    color =
                        Color.Gray
                )
            }

            Icon(
                imageVector =
                    Icons.Default.ArrowForward,

                contentDescription =
                    "Open $title",

                tint =
                    Color(0xFF2E7D32)
            )
        }
    }
}