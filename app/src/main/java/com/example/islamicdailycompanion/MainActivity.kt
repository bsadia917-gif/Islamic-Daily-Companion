package com.example.islamicdailycompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicdailycompanion.ui.theme.IslamicDailyCompanionTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            IslamicDailyCompanionTheme {

                var showSplash by remember {
                    mutableStateOf(true)
                }

                if (showSplash) {

                    SplashScreen(
                        onSplashFinished = {
                            showSplash = false
                        }
                    )

                } else {

                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {

    LaunchedEffect(Unit) {

        delay(2500)

        onSplashFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFE8F5E9)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🌙",
            fontSize = 60.sp
        )

        Text(
            text = "Islamic Daily Companion",
            fontSize = 24.sp
        )

        Text(
            text = "Your Daily Islamic Companion",
            fontSize = 14.sp
        )
    }
}


@Composable
fun WelcomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFE8F5E9)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🌙",
            fontSize = 60.sp
        )

        Text(
            text = "Welcome to",
            fontSize = 26.sp
        )

        Text(
            text = "Islamic Daily Companion",
            fontSize = 26.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            onClick = onSignInClick
        ) {
            Text("Sign In")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = onSignUpClick
        ) {
            Text("Sign Up")
        }
    }
}