
package com.example.islamicdailycompanion

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignInScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "🌙",
            fontSize = 50.sp
        )

        Text(
            text = "Welcome Back",
            fontSize = 28.sp
        )

        Text(
            text = "Islamic Daily Companion",
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // =========================
        // EMAIL
        // =========================

        OutlinedTextField(

            value = email,

            onValueChange = {
                email = it
                errorMessage = ""
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Email")
            },

            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================
        // PASSWORD
        // =========================

        OutlinedTextField(

            value = password,

            onValueChange = {
                password = it
                errorMessage = ""
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Password")
            },

            singleLine = true,

            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {

                IconButton(
                    onClick = {
                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Icon(

                        imageVector =
                            if (passwordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },

                        contentDescription =
                            "Show or hide password"
                    )
                }
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================
        // ERROR MESSAGE
        // =========================

        if (errorMessage.isNotEmpty()) {

            Text(

                text = errorMessage,

                color = Color.Red,

                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        // =========================
        // SIGN IN BUTTON
        // =========================

        Button(

            onClick = {

                when {

                    email.isBlank() -> {

                        errorMessage =
                            "Please enter your email"
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches() -> {

                        errorMessage =
                            "Please enter a valid email"
                    }

                    password.isBlank() -> {

                        errorMessage =
                            "Please enter your password"
                    }

                    else -> {

                        isLoading = true
                        errorMessage = ""

                        auth.signInWithEmailAndPassword(
                            email.trim(),
                            password
                        )
                            .addOnCompleteListener { task ->

                                isLoading = false

                                if (task.isSuccessful) {

                                    onSignInClick()

                                } else {

                                    errorMessage =
                                        task.exception
                                            ?.message
                                            ?: "Sign in failed"
                                }
                            }
                    }
                }
            },

            modifier = Modifier.fillMaxWidth(),

            enabled = !isLoading
        ) {

            Text(

                text =
                    if (isLoading) {
                        "Signing In..."
                    } else {
                        "Sign In"
                    }
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================
        // SIGN UP BUTTON
        // =========================

        Button(

            onClick = onSignUpClick,

            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                "Don't have an account? Sign Up"
            )
        }
    }
}