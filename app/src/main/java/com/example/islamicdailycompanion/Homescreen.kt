package com.example.islamicdailycompanion

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.*
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

import androidx.core.content.ContextCompat

import kotlinx.coroutines.delay

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(
    onQuranClick: () -> Unit,
    onDuaClick: () -> Unit,
    onTasbeehClick: () -> Unit,
    onPrayerClick: () -> Unit,
    onQuranTrackerClick: () -> Unit,
    onIslamicCalendarClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    val context = LocalContext.current

    // =====================================
    // USER PROFILE
    // =====================================

    val preferences = remember {
        context.getSharedPreferences(
            "user_profile",
            Context.MODE_PRIVATE
        )
    }

    var profileBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(Unit) {

        val imageUriString =
            preferences.getString(
                "profile_image_uri",
                null
            )

        if (!imageUriString.isNullOrEmpty()) {

            try {

                val uri = Uri.parse(imageUriString)

                val inputStream =
                    context.contentResolver
                        .openInputStream(uri)

                profileBitmap =
                    BitmapFactory.decodeStream(
                        inputStream
                    )

                inputStream?.close()

            } catch (e: Exception) {

                profileBitmap = null
            }
        }
    }

    val userName =
        preferences.getString(
            "user_name",
            ""
        ) ?: ""


    // =====================================
    // CURRENT TIME
    // =====================================

    var currentTime by remember {
        mutableStateOf(Date())
    }

    LaunchedEffect(Unit) {

        while (true) {

            currentTime = Date()

            delay(1000)
        }
    }


    // =====================================
    // LOCATION
    // =====================================

    var location by remember {
        mutableStateOf<Location?>(null)
    }

    var locationError by remember {
        mutableStateOf(false)
    }

    val locationLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permissions[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (granted) {

                getFreshLocation(
                    context = context,
                    onLocationReceived = {
                        location = it
                        locationError = false
                    },
                    onError = {
                        locationError = true
                    }
                )

            } else {

                locationError = true
            }
        }


    LaunchedEffect(Unit) {

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {

            getFreshLocation(
                context = context,
                onLocationReceived = {
                    location = it
                    locationError = false
                },
                onError = {
                    locationError = true
                }
            )

        } else {

            locationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    // =====================================
    // PRAYER TIMES
    // =====================================

    val prayerTimes =
        location?.let {
            calculatePrayerTimes(it)
        } ?: emptyList()


    // =====================================
    // NEXT PRAYER
    // =====================================

    var nextPrayer =
        getNextPrayer(
            prayerTimes,
            currentTime
        )


    // =====================================
    // AFTER ISHA → TOMORROW FAJR
    // =====================================

    if (
        nextPrayer == null &&
        location != null
    ) {

        nextPrayer =
            calculateTomorrowFajr(
                location!!
            )
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFF7FBF7)
            ),
        verticalArrangement =
            Arrangement.spacedBy(16.dp),
        contentPadding =
            PaddingValues(
                bottom = 25.dp
            )
    ) {


        // =================================
        // HEADER
        // =================================

        item {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush =
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1B5E20),
                                    Color(0xFF388E3C),
                                    Color(0xFF66BB6A)
                                )
                            ),
                        shape =
                            RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp
                            )
                    )
                    .padding(
                        start = 20.dp,
                        end = 14.dp,
                        top = 28.dp,
                        bottom = 28.dp
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 60.dp)
                ) {

                    Text(
                        text =
                            "Assalamu Alaikum 🌙",
                        fontSize = 25.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color.White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            if (userName.isNotBlank()) {
                                "Good Morning, $userName"
                            } else {
                                "Good Morning"
                            },
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color =
                            Color.White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "May Allah bless your day 🤍",
                        fontSize = 14.sp,
                        color =
                            Color.White.copy(
                                alpha = 0.9f
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text = today,
                        fontSize = 13.sp,
                        color =
                            Color.White.copy(
                                alpha = 0.85f
                            )
                    )
                }


                // PROFILE BUTTON

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
                                Modifier.size(31.dp)
                        )
                    }
                }
            }
        }


        // =================================
        // NEXT PRAYER
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
                    RoundedCornerShape(24.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFB2DFDB)
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 4.dp
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
                            "🕌 Next Prayer",
                        fontSize = 17.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color =
                            Color(0xFF245A4A)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )


                    if (nextPrayer != null) {

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text =
                                    nextPrayer!!.icon,
                                fontSize = 42.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(14.dp)
                            )


                            Column(
                                modifier =
                                    Modifier.weight(1f)
                            ) {

                                Text(
                                    text =
                                        nextPrayer!!.name,
                                    fontSize = 26.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color =
                                        Color(0xFF164D3D)
                                )

                                Text(
                                    text =
                                        nextPrayer!!.time,
                                    fontSize = 17.sp,
                                    color =
                                        Color.DarkGray
                                )
                            }


                            Column(
                                horizontalAlignment =
                                    Alignment.End
                            ) {

                                Text(
                                    text =
                                        "Remaining",
                                    fontSize = 12.sp,
                                    color =
                                        Color.Gray
                                )

                                Text(
                                    text =
                                        getRemainingTime(
                                            nextPrayer!!.date,
                                            currentTime
                                        ),
                                    fontSize = 15.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color =
                                        Color(0xFF1B5E20)
                                )
                            }
                        }

                    } else {

                        Text(
                            text =
                                if (locationError) {
                                    "Location required for prayer times"
                                } else {
                                    "Getting prayer times..."
                                },
                            fontSize = 15.sp,
                            color =
                                Color.Gray
                        )
                    }
                }
            }
        }


        // =================================
        // QUICK ACCESS
        // =================================

        item {

            Text(
                text = "Quick Access",
                modifier =
                    Modifier.padding(
                        horizontal = 18.dp
                    ),
                fontSize = 22.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF16351A)
            )
        }


        // =================================
        // FIRST ROW
        // =================================

        item {

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        ),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                HomeGridCard(
                    icon = "📿",
                    title = "Tasbeeh",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onTasbeehClick
                )

                HomeGridCard(
                    icon = "🤲",
                    title = "Daily Duas",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onDuaClick
                )

                HomeGridCard(
                    icon = "📖",
                    title = "Quran Tracker",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onQuranTrackerClick
                )
            }
        }


        // =================================
        // SECOND ROW
        // =================================

        item {

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp
                        ),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                HomeGridCard(
                    icon = "🕌",
                    title = "Prayer",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onPrayerClick
                )

                HomeGridCard(
                    icon = "📖",
                    title = "Quran",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onQuranClick
                )

                HomeGridCard(
                    icon = "📅",
                    title = "Islamic Calendar",
                    modifier =
                        Modifier.weight(1f),
                    onClick =
                        onIslamicCalendarClick
                )
            }
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
                            top = 8.dp
                        ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "Islamic Daily Companion",
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Medium,
                    color =
                        Color.DarkGray
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        "May Allah bless your day 🤍",
                    fontSize = 13.sp,
                    color =
                        Color.Gray
                )
            }
        }
    }
}


// =====================================
// GRID CARD
// =====================================

@Composable
fun HomeGridCard(
    icon: String,
    title: String,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier =
            modifier
                .height(125.dp)
                .clickable {
                    onClick()
                },
        shape =
            RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Box(
                modifier =
                    Modifier
                        .size(52.dp)
                        .background(
                            Color(0xFFE8F5E9),
                            RoundedCornerShape(16.dp)
                        ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = icon,
                    fontSize = 28.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight =
                    FontWeight.SemiBold,
                color =
                    Color(0xFF1B5E20)
            )
        }
    }
}


// =====================================
// TOMORROW FAJR
// =====================================

fun calculateTomorrowFajr(
    location: Location
): PrayerTime {

    val coordinates =
        com.batoulapps.adhan.Coordinates(
            location.latitude,
            location.longitude
        )

    val calendar =
        Calendar.getInstance()

    calendar.add(
        Calendar.DAY_OF_YEAR,
        1
    )

    val date =
        com.batoulapps.adhan.data.DateComponents(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

    val params =
        com.batoulapps.adhan.CalculationMethod
            .KARACHI
            .getParameters()

    val prayerTimes =
        com.batoulapps.adhan.PrayerTimes(
            coordinates,
            date,
            params
        )

    return PrayerTime(
        name = "Fajr",
        time = formatTime(
            prayerTimes.fajr
        ),
        icon = "🌅",
        date = prayerTimes.fajr
    )
}