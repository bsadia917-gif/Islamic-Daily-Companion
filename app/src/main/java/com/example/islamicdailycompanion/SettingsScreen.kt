
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SettingsScreen() {

    val context = androidx.compose.ui.platform.LocalContext.current

    val preferences = remember {

        context.getSharedPreferences(
            "app_settings",
            Context.MODE_PRIVATE
        )
    }


    // =====================================
    // NOTIFICATION SETTING
    // =====================================

    var notificationsEnabled by remember {

        mutableStateOf(
            preferences.getBoolean(
                "notifications_enabled",
                false
            )
        )
    }


    // =====================================
    // DARK MODE SETTING
    // =====================================

    var darkModeEnabled by remember {

        mutableStateOf(
            preferences.getBoolean(
                "dark_mode_enabled",
                false
            )
        )
    }


    // =====================================
    // UI
    // =====================================

    Column(

        modifier =
            Modifier
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
                .padding(20.dp)
    ) {


        // =================================
        // TITLE
        // =================================

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            androidx.compose.material3.Icon(

                imageVector =
                    Icons.Default.Settings,

                contentDescription =
                    "Settings",

                modifier =
                    Modifier.size(35.dp),

                tint =
                    Color(0xFF2E7D32)
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Text(

                text =
                    "Settings",

                fontSize =
                    30.sp
            )
        }


        Spacer(
            modifier =
                Modifier.height(6.dp)
        )


        Text(

            text =
                "Customize your Islamic Daily Companion",

            fontSize =
                15.sp,

            color =
                Color.Gray
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // NOTIFICATIONS
        // =================================

        SettingsSwitchCard(

            icon =
                Icons.Default.Notifications,

            title =
                "Notifications",

            description =
                "Enable app notifications",

            checked =
                notificationsEnabled,

            onCheckedChange = {

                notificationsEnabled =
                    it

                preferences.edit()
                    .putBoolean(
                        "notifications_enabled",
                        it
                    )
                    .apply()
            }
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =================================
        // DARK MODE
        // =================================

        SettingsSwitchCard(

            icon =
                Icons.Default.DarkMode,

            title =
                "Dark Mode",

            description =
                "Use dark appearance",

            checked =
                darkModeEnabled,

            onCheckedChange = {

                darkModeEnabled =
                    it

                preferences.edit()
                    .putBoolean(
                        "dark_mode_enabled",
                        it
                    )
                    .apply()
            }
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // APP INFORMATION
        // =================================

        Text(

            text =
                "App Information",

            fontSize =
                21.sp
        )


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        Card(

            modifier =
                Modifier.fillMaxWidth(),

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

            Row(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(18.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                androidx.compose.material3.Icon(

                    imageVector =
                        Icons.Default.Info,

                    contentDescription =
                        "App Information",

                    modifier =
                        Modifier.size(32.dp),

                    tint =
                        Color(0xFF2E7D32)
                )


                Spacer(
                    modifier =
                        Modifier.width(15.dp)
                )


                Column {

                    Text(

                        text =
                            "Islamic Daily Companion",

                        fontSize =
                            18.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(

                        text =
                            "Version 1.0",

                        fontSize =
                            14.sp,

                        color =
                            Color.Gray
                    )
                }
            }
        }


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // NOTE
        // =================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(20.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFE8F5E9)
                )
        ) {

            Column(

                modifier =
                    Modifier.padding(18.dp)
            ) {

                Text(

                    text =
                        "🕌 Islamic Daily Companion",

                    fontSize =
                        18.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(

                    text =
                        "More useful Islamic features will be added step by step.",

                    fontSize =
                        14.sp
                )
            }
        }
    }
}


// =========================================
// SETTINGS SWITCH CARD
// =========================================

@Composable
fun SettingsSwitchCard(

    icon:
    androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    description: String,

    checked: Boolean,

    onCheckedChange:
        (Boolean) -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

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

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            androidx.compose.material3.Icon(

                imageVector =
                    icon,

                contentDescription =
                    title,

                modifier =
                    Modifier.size(32.dp),

                tint =
                    Color(0xFF2E7D32)
            )


            Spacer(
                modifier =
                    Modifier.width(15.dp)
            )


            Column(

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        title,

                    fontSize =
                        18.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(

                    text =
                        description,

                    fontSize =
                        13.sp,

                    color =
                        Color.Gray
                )
            }


            Switch(

                checked =
                    checked,

                onCheckedChange =
                    onCheckedChange
            )
        }
    }
}

