
package com.example.islamicdailycompanion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class PrayerTime(
    val name: String,
    val time: String,
    val icon: String,
    val date: Date
)

@Composable
fun PrayerScreen() {

    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "prayer_history",
            Context.MODE_PRIVATE
        )
    }

    val today = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())

    var location by remember {
        mutableStateOf<Location?>(null)
    }

    var cityName by remember {
        mutableStateOf("Getting location...")
    }

    var locationError by remember {
        mutableStateOf("")
    }

    var gpsEnabled by remember {
        mutableStateOf(isLocationEnabled(context))
    }

    var currentTime by remember {
        mutableStateOf(Date())
    }

    // =====================================
    // FRESH LOCATION
    // =====================================

    val locationLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {

                if (isLocationEnabled(context)) {

                    locationError = ""

                    getFreshLocation(
                        context = context,
                        onLocationReceived = {
                            location = it
                        },
                        onError = {
                            locationError = it
                        }
                    )

                } else {

                    locationError =
                        "Please turn ON your phone's Location/GPS."
                }

            } else {

                locationError =
                    "Location permission is required to calculate prayer times."
            }
        }

    // =====================================
    // REQUEST LOCATION
    // =====================================

    fun requestLocation() {

        gpsEnabled = isLocationEnabled(context)

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

        if (!fineGranted && !coarseGranted) {

            locationLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        } else if (!gpsEnabled) {

            locationError =
                "Please turn ON your phone's Location/GPS."

        } else {

            locationError = ""

            getFreshLocation(
                context = context,
                onLocationReceived = {
                    location = it
                },
                onError = {
                    locationError = it
                }
            )
        }
    }

    // =====================================
    // INITIAL LOCATION
    // =====================================

    LaunchedEffect(Unit) {
        requestLocation()
    }

    // =====================================
    // REFRESH LOCATION WHEN SCREEN RESUMES
    // =====================================

    LaunchedEffect(Unit) {

        while (true) {

            delay(5000)

            gpsEnabled =
                isLocationEnabled(context)

            if (gpsEnabled) {

                val permissionGranted =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                if (permissionGranted) {

                    getFreshLocation(
                        context = context,
                        onLocationReceived = {
                            location = it
                        },
                        onError = {
                            locationError = it
                        }
                    )
                }
            }
        }
    }

    // =====================================
    // CITY NAME
    // =====================================

    LaunchedEffect(location) {

        location?.let {

            cityName =
                getCityName(
                    context,
                    it.latitude,
                    it.longitude
                )
        }
    }

    // =====================================
    // CURRENT TIME
    // =====================================

    LaunchedEffect(Unit) {

        while (true) {

            currentTime = Date()

            delay(1000)
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

    val nextPrayer =
        getNextPrayer(
            prayerTimes,
            currentTime
        )

    // =====================================
    // PRAYER STATUS
    // =====================================

    var fajrDone by remember {
        mutableStateOf(
            preferences.getBoolean(
                "${today}_Fajr",
                false
            )
        )
    }

    var dhuhrDone by remember {
        mutableStateOf(
            preferences.getBoolean(
                "${today}_Dhuhr",
                false
            )
        )
    }

    var asrDone by remember {
        mutableStateOf(
            preferences.getBoolean(
                "${today}_Asr",
                false
            )
        )
    }

    var maghribDone by remember {
        mutableStateOf(
            preferences.getBoolean(
                "${today}_Maghrib",
                false
            )
        )
    }

    var ishaDone by remember {
        mutableStateOf(
            preferences.getBoolean(
                "${today}_Isha",
                false
            )
        )
    }

    val prayersCompleted =
        listOf(
            fajrDone,
            dhuhrDone,
            asrDone,
            maghribDone,
            ishaDone
        ).count { it }

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
            .padding(16.dp)
    ) {

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // =================================
            // HEADER
            // =================================

            item {

                Text(
                    text = "🕌 Prayer Times",
                    fontSize = 30.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text =
                        "Your daily Salah companion",
                    fontSize = 16.sp
                )
            }

            // =================================
            // LOCATION CARD
            // =================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(20.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFE0F2F1)
                        )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                    ) {

                        Text(
                            text =
                                "📍 Your Location",
                            fontSize = 17.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(
                            text =
                                cityName,
                            fontSize = 21.sp
                        )

                        if (location != null) {

                            Spacer(
                                modifier =
                                    Modifier.height(5.dp)
                            )

                            Text(
                                text =
                                    "Location detected successfully",
                                fontSize = 13.sp,
                                color =
                                    Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }

            // =================================
            // LOCATION ERROR / ENABLE
            // =================================

            item {

                if (
                    location == null ||
                    locationError.isNotEmpty()
                ) {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text =
                                    if (!gpsEnabled)
                                        "📍 Location is OFF"
                                    else
                                        "📍 Location Required",
                                fontSize = 19.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(7.dp)
                            )

                            Text(
                                text =
                                    if (locationError.isNotEmpty())
                                        locationError
                                    else
                                        "Allow location access to calculate accurate prayer times.",
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Button(
                                onClick = {

                                    if (!isLocationEnabled(context)) {

                                        context.startActivity(
                                            Intent(
                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                            )
                                        )

                                    } else {

                                        requestLocation()
                                    }
                                }
                            ) {

                                Text(
                                    text =
                                        if (!gpsEnabled)
                                            "Turn ON Location"
                                        else
                                            "Allow Location"
                                )
                            }
                        }
                    }
                }
            }

            // =================================
            // NEXT PRAYER
            // =================================

            item {

                if (nextPrayer != null) {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(24.dp),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color(0xFFB2DFDB)
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(22.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Text(
                                text =
                                    "⏳ Next Prayer",
                                fontSize = 18.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(7.dp)
                            )

                            Text(
                                text =
                                    "${nextPrayer.icon} ${nextPrayer.name}",
                                fontSize = 27.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    nextPrayer.time,
                                fontSize = 20.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "Remaining: ${
                                        getRemainingTime(
                                            nextPrayer.date,
                                            currentTime
                                        )
                                    }",
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }

            // =================================
            // TODAY SUMMARY
            // =================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(22.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color(0xFFE8F5E9)
                        )
                ) {

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "Today's Salah",
                            fontSize = 20.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(
                            text =
                                "$prayersCompleted / 5",
                            fontSize = 35.sp
                        )

                        Text(
                            text =
                                if (
                                    prayersCompleted == 5
                                )
                                    "🌟 All prayers completed!"
                                else
                                    "Keep going 🤲",
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // =================================
            // DATE
            // =================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(18.dp)
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
                            text =
                                "Today",
                            fontSize = 17.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                today,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // =================================
            // PRAYER LIST
            // =================================

            item {

                Text(
                    text =
                        "Today's Prayers",
                    fontSize = 22.sp
                )
            }

            item {

                PrayerSimpleCard(
                    name = "Fajr",
                    icon = "🌅",
                    time =
                        getPrayerTime(
                            prayerTimes,
                            "Fajr"
                        ),
                    completed = fajrDone,
                    onClick = {

                        fajrDone = !fajrDone

                        preferences.edit()
                            .putBoolean(
                                "${today}_Fajr",
                                fajrDone
                            )
                            .apply()
                    }
                )
            }

            item {

                PrayerSimpleCard(
                    name = "Dhuhr",
                    icon = "☀️",
                    time =
                        getPrayerTime(
                            prayerTimes,
                            "Dhuhr"
                        ),
                    completed = dhuhrDone,
                    onClick = {

                        dhuhrDone = !dhuhrDone

                        preferences.edit()
                            .putBoolean(
                                "${today}_Dhuhr",
                                dhuhrDone
                            )
                            .apply()
                    }
                )
            }

            item {

                PrayerSimpleCard(
                    name = "Asr",
                    icon = "🌤️",
                    time =
                        getPrayerTime(
                            prayerTimes,
                            "Asr"
                        ),
                    completed = asrDone,
                    onClick = {

                        asrDone = !asrDone

                        preferences.edit()
                            .putBoolean(
                                "${today}_Asr",
                                asrDone
                            )
                            .apply()
                    }
                )
            }

            item {

                PrayerSimpleCard(
                    name = "Maghrib",
                    icon = "🌇",
                    time =
                        getPrayerTime(
                            prayerTimes,
                            "Maghrib"
                        ),
                    completed = maghribDone,
                    onClick = {

                        maghribDone = !maghribDone

                        preferences.edit()
                            .putBoolean(
                                "${today}_Maghrib",
                                maghribDone
                            )
                            .apply()
                    }
                )
            }

            item {

                PrayerSimpleCard(
                    name = "Isha",
                    icon = "🌙",
                    time =
                        getPrayerTime(
                            prayerTimes,
                            "Isha"
                        ),
                    completed = ishaDone,
                    onClick = {

                        ishaDone = !ishaDone

                        preferences.edit()
                            .putBoolean(
                                "${today}_Isha",
                                ishaDone
                            )
                            .apply()
                    }
                )
            }

            // =================================
            // PRAYER TIMES
            // =================================

            item {

                if (location != null) {

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
                                    .padding(18.dp)
                        ) {

                            Text(
                                text =
                                    "🕌 Today's Prayer Times",
                                fontSize = 21.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    cityName,
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            prayerTimes.forEach { prayer ->

                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = 7.dp
                                            ),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text =
                                            "${prayer.icon} ${prayer.name}",
                                        fontSize = 17.sp
                                    )

                                    Text(
                                        text =
                                            prayer.time,
                                        fontSize = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // =================================
            // MONTHLY RECORD
            // =================================

            item {

                MonthlyPrayerRecord(
                    preferences =
                        preferences
                )
            }
        }
    }
}


// =====================================
// PRAYER CARD
// =====================================

@Composable
fun PrayerSimpleCard(
    name: String,
    icon: String,
    time: String,
    completed: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (completed)
                        Color(0xFFE8F5E9)
                    else
                        Color.White
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = icon,
                    fontSize = 29.sp
                )

                Spacer(
                    modifier =
                        Modifier.width(13.dp)
                )

                Column {

                    Text(
                        text = name,
                        fontSize = 19.sp
                    )

                    Text(
                        text = time,
                        fontSize = 14.sp
                    )
                }
            }

            Switch(
                checked = completed,
                onCheckedChange = {
                    onClick()
                }
            )
        }
    }
}


// =====================================
// NEXT PRAYER
// =====================================

fun getNextPrayer(
    prayers: List<PrayerTime>,
    currentTime: Date
): PrayerTime? {

    return prayers.firstOrNull {
        it.date.after(currentTime)
    }
}


// =====================================
// REMAINING TIME
// =====================================

fun getRemainingTime(
    prayerDate: Date,
    currentTime: Date
): String {

    var difference =
        prayerDate.time - currentTime.time

    if (difference < 0) {
        difference = 0
    }

    val totalSeconds =
        difference / 1000

    val hours =
        totalSeconds / 3600

    val minutes =
        (totalSeconds % 3600) / 60

    val seconds =
        totalSeconds % 60

    return String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d",
        hours,
        minutes,
        seconds
    )
}


// =====================================
// GET PRAYER TIME
// =====================================

fun getPrayerTime(
    prayers: List<PrayerTime>,
    name: String
): String {

    return prayers
        .find {
            it.name == name
        }
        ?.time
        ?: "--:--"
}


// =====================================
// MONTHLY RECORD
// =====================================

@Composable
fun MonthlyPrayerRecord(
    preferences: android.content.SharedPreferences
) {

    val calendar =
        Calendar.getInstance()

    val month =
        calendar.get(Calendar.MONTH)

    val year =
        calendar.get(Calendar.YEAR)

    var fajrCount = 0
    var dhuhrCount = 0
    var asrCount = 0
    var maghribCount = 0
    var ishaCount = 0

    val daysInMonth =
        calendar.getActualMaximum(
            Calendar.DAY_OF_MONTH
        )

    for (day in 1..daysInMonth) {

        val date =
            String.format(
                Locale.getDefault(),
                "%04d-%02d-%02d",
                year,
                month + 1,
                day
            )

        if (
            preferences.getBoolean(
                "${date}_Fajr",
                false
            )
        ) fajrCount++

        if (
            preferences.getBoolean(
                "${date}_Dhuhr",
                false
            )
        ) dhuhrCount++

        if (
            preferences.getBoolean(
                "${date}_Asr",
                false
            )
        ) asrCount++

        if (
            preferences.getBoolean(
                "${date}_Maghrib",
                false
            )
        ) maghribCount++

        if (
            preferences.getBoolean(
                "${date}_Isha",
                false
            )
        ) ishaCount++
    }

    val total =
        fajrCount +
                dhuhrCount +
                asrCount +
                maghribCount +
                ishaCount

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
                    .padding(20.dp)
        ) {

            Text(
                text =
                    "📊 Monthly Prayer Record",
                fontSize = 21.sp
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(
                text =
                    SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                    ).format(Date()),
                fontSize = 15.sp
            )

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            PrayerMonthRow(
                "🌅 Fajr",
                fajrCount,
                daysInMonth
            )

            PrayerMonthRow(
                "☀️ Dhuhr",
                dhuhrCount,
                daysInMonth
            )

            PrayerMonthRow(
                "🌤️ Asr",
                asrCount,
                daysInMonth
            )

            PrayerMonthRow(
                "🌇 Maghrib",
                maghribCount,
                daysInMonth
            )

            PrayerMonthRow(
                "🌙 Isha",
                ishaCount,
                daysInMonth
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            PrayerMonthRow(
                "Total",
                total,
                daysInMonth * 5
            )
        }
    }
}


// =====================================
// MONTH ROW
// =====================================

@Composable
fun PrayerMonthRow(
    title: String,
    count: Int,
    total: Int
) {

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp
                ),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            fontSize = 16.sp
        )

        Text(
            text = "$count / $total",
            fontSize = 16.sp
        )
    }
}


// =====================================
// LOCATION ENABLED?
// =====================================

fun isLocationEnabled(
    context: Context
): Boolean {

    val locationManager =
        context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

    return try {

        locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        ) ||
                locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )

    } catch (
        e: Exception
    ) {

        false
    }
}


// =====================================
// FRESH LOCATION
// =====================================

fun getFreshLocation(
    context: Context,
    onLocationReceived: (Location) -> Unit,
    onError: (String) -> Unit
) {

    val hasFinePermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    val hasCoarsePermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    if (!hasFinePermission && !hasCoarsePermission) {

        onError(
            "Location permission is not granted."
        )

        return
    }

    val locationManager =
        context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

    try {

        val providers =
            locationManager.getProviders(true)

        var bestLocation: Location? = null

        for (provider in providers) {

            val lastLocation =
                locationManager.getLastKnownLocation(
                    provider
                )

            if (
                lastLocation != null &&
                (
                        bestLocation == null ||
                                lastLocation.accuracy <
                                bestLocation!!.accuracy
                        )
            ) {

                bestLocation =
                    lastLocation
            }
        }

        if (bestLocation != null) {

            onLocationReceived(
                bestLocation
            )

        } else {

            onError(
                "Unable to get your location. Please move to an open area and try again."
            )
        }

    } catch (
        e: SecurityException
    ) {

        onError(
            "Location permission is required."
        )

    } catch (
        e: Exception
    ) {

        onError(
            "Unable to get location."
        )
    }
}


// =====================================
// CITY NAME
// =====================================

suspend fun getCityName(
    context: Context,
    latitude: Double,
    longitude: Double
): String {

    return withContext(
        Dispatchers.IO
    ) {

        try {

            val geocoder =
                Geocoder(
                    context,
                    Locale.getDefault()
                )

            val addresses =
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )

            if (
                !addresses.isNullOrEmpty()
            ) {

                val address =
                    addresses[0]

                val city =
                    address.locality
                        ?: address.subAdminArea

                val country =
                    address.countryName

                if (
                    !city.isNullOrEmpty() &&
                    !country.isNullOrEmpty()
                ) {

                    "$city, $country"

                } else if (
                    !city.isNullOrEmpty()
                ) {

                    city

                } else {

                    "Location found"
                }

            } else {

                "Location found"
            }

        } catch (
            e: Exception
        ) {

            "Location found"
        }
    }
}


// =====================================
// CALCULATE PRAYER TIMES
// =====================================

fun calculatePrayerTimes(
    location: Location
): List<PrayerTime> {

    val coordinates =
        Coordinates(
            location.latitude,
            location.longitude
        )

    val calendar =
        Calendar.getInstance()

    val date =
        DateComponents(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

    val params =
        CalculationMethod
            .KARACHI
            .getParameters()

    val prayerTimes =
        PrayerTimes(
            coordinates,
            date,
            params
        )

    return listOf(

        PrayerTime(
            "Fajr",
            formatTime(prayerTimes.fajr),
            "🌅",
            prayerTimes.fajr
        ),

        PrayerTime(
            "Dhuhr",
            formatTime(prayerTimes.dhuhr),
            "☀️",
            prayerTimes.dhuhr
        ),

        PrayerTime(
            "Asr",
            formatTime(prayerTimes.asr),
            "🌤️",
            prayerTimes.asr
        ),

        PrayerTime(
            "Maghrib",
            formatTime(prayerTimes.maghrib),
            "🌇",
            prayerTimes.maghrib
        ),

        PrayerTime(
            "Isha",
            formatTime(prayerTimes.isha),
            "🌙",
            prayerTimes.isha
        )
    )
}


// =====================================
// FORMAT TIME
// =====================================

fun formatTime(
    date: Date
): String {

    val calendar =
        Calendar.getInstance()

    calendar.time =
        date

    val hour =
        calendar.get(Calendar.HOUR)

    val minute =
        calendar.get(Calendar.MINUTE)

    val actualHour =
        if (hour == 0) 12 else hour

    val amPm =
        if (
            calendar.get(Calendar.AM_PM)
            == Calendar.AM
        )
            "AM"
        else
            "PM"

    return String.format(
        Locale.getDefault(),
        "%02d:%02d %s",
        actualHour,
        minute,
        amPm
    )
}
