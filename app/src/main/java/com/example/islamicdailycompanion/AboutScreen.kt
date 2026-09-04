
package com.example.islamicdailycompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AboutScreen() {

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
                .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {


        // =================================
        // TITLE
        // =================================

        Text(

            text =
                "ℹ️ About",

            fontSize =
                30.sp,

            fontWeight =
                FontWeight.Bold
        )


        Spacer(
            modifier =
                Modifier.height(6.dp)
        )


        Text(

            text =
                "Islamic Daily Companion",

            fontSize =
                17.sp
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // APP ICON
        // =================================

        Card(

            modifier =
                Modifier.size(120.dp),

            shape =
                RoundedCornerShape(30.dp),

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

                    text =
                        "🕌",

                    fontSize =
                        55.sp
                )
            }
        }


        Spacer(
            modifier =
                Modifier.height(18.dp)
        )


        Text(

            text =
                "Islamic Daily Companion",

            fontSize =
                24.sp,

            fontWeight =
                FontWeight.Bold
        )


        Text(

            text =
                "Version 1.0",

            fontSize =
                14.sp,

            color =
                Color.Gray
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // ABOUT APP
        // =================================

        AboutCard(

            icon =
                Icons.Default.Info,

            title =
                "About the App",

            description =
                "Islamic Daily Companion is designed to help Muslims stay connected with their daily Islamic activities in a simple and organized way."
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =================================
        // FEATURES
        // =================================

        AboutCard(

            icon =
                Icons.Default.MenuBook,

            title =
                "Available Features",

            description =
                "Quran reading, Daily Duas, Digital Tasbeeh, Prayer Times, Prayer History, Profile and App Settings."
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =================================
        // PURPOSE
        // =================================

        AboutCard(

            icon =
                Icons.Default.Favorite,

            title =
                "Our Purpose",

            description =
                "Our goal is to make useful Islamic tools easily accessible and encourage users to build positive daily habits."
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // FOOTER
        // =================================

        Text(

            text =
                "🕌 Islamic Daily Companion",

            fontSize =
                17.sp,

            fontWeight =
                FontWeight.SemiBold
        )


        Spacer(
            modifier =
                Modifier.height(5.dp)
        )


        Text(

            text =
                "May Allah bless your journey 🤲",

            fontSize =
                14.sp,

            color =
                Color.Gray
        )
    }
}


// =========================================
// ABOUT CARD
// =========================================

@Composable
fun AboutCard(

    icon:
    androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    description: String

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
                Alignment.Top
        ) {

            Icon(

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


            Column {

                Text(

                    text =
                        title,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )


                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )


                Text(

                    text =
                        description,

                    fontSize =
                        14.sp,

                    color =
                        Color.Gray
                )
            }
        }
    }
}

