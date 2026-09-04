
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
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit,
    onSignUpSuccess: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val auth = remember {
        FirebaseAuth.getInstance()
    }


    // =====================================
    // UI
    // =====================================

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {


        // =================================
        // LOGO
        // =================================

        Text(
            text = "🌙",
            fontSize = 50.sp
        )

        Text(
            text = "Create Account",
            fontSize = 28.sp
        )

        Text(
            text = "Islamic Daily Companion",
            fontSize = 18.sp
        )


        Spacer(
            modifier =
                Modifier.height(25.dp)
        )


        // =================================
        // NAME
        // =================================

        OutlinedTextField(

            value = name,

            onValueChange = {

                name = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Name")
            },

            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        // =================================
        // EMAIL
        // =================================

        OutlinedTextField(

            value = email,

            onValueChange = {

                email = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Email")
            },

            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.height(10.dp)
        )


        // =================================
        // PASSWORD
        // =================================

        OutlinedTextField(

            value = password,

            onValueChange = {

                password = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

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
            modifier =
                Modifier.height(10.dp)
        )


        // =================================
        // CONFIRM PASSWORD
        // =================================

        OutlinedTextField(

            value = confirmPassword,

            onValueChange = {

                confirmPassword = it
                errorMessage = ""
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Confirm Password")
            },

            singleLine = true,

            visualTransformation =

                if (confirmPasswordVisible) {

                    VisualTransformation.None

                } else {

                    PasswordVisualTransformation()
                },

            trailingIcon = {

                IconButton(

                    onClick = {

                        confirmPasswordVisible =
                            !confirmPasswordVisible
                    }

                ) {

                    Icon(

                        imageVector =

                            if (confirmPasswordVisible) {

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
            modifier =
                Modifier.height(12.dp)
        )


        // =================================
        // ERROR MESSAGE
        // =================================

        if (errorMessage.isNotEmpty()) {

            Text(

                text =
                    errorMessage,

                color =
                    Color.Red,

                fontSize =
                    14.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }


        // =================================
        // SIGN UP BUTTON
        // =================================

        Button(

            onClick = {

                when {

                    // NAME
                    name.isBlank() -> {

                        errorMessage =
                            "Please enter your name"
                    }


                    // EMAIL
                    !Patterns.EMAIL_ADDRESS
                        .matcher(
                            email.trim()
                        )
                        .matches() -> {

                        errorMessage =
                            "Please enter a valid email"
                    }


                    // PASSWORD
                    password.length < 6 -> {

                        errorMessage =
                            "Password must be at least 6 characters"
                    }


                    // CONFIRM PASSWORD
                    confirmPassword.isBlank() -> {

                        errorMessage =
                            "Please confirm your password"
                    }


                    // PASSWORD MATCH
                    password != confirmPassword -> {

                        errorMessage =
                            "Passwords do not match"
                    }


                    // CREATE ACCOUNT
                    else -> {

                        isLoading =
                            true

                        errorMessage =
                            ""


                        auth
                            .createUserWithEmailAndPassword(
                                email.trim(),
                                password
                            )
                            .addOnCompleteListener { task ->


                                if (task.isSuccessful) {


                                    // =========================
                                    // SAVE USER NAME
                                    // =========================

                                    val user =
                                        auth.currentUser


                                    val profileUpdates =
                                        UserProfileChangeRequest
                                            .Builder()
                                            .setDisplayName(
                                                name.trim()
                                            )
                                            .build()


                                    user
                                        ?.updateProfile(
                                            profileUpdates
                                        )
                                        ?.addOnCompleteListener {

                                            isLoading =
                                                false

                                            onSignUpSuccess()
                                        }

                                        ?: run {

                                            isLoading =
                                                false

                                            errorMessage =
                                                "User profile could not be created"
                                        }


                                } else {

                                    isLoading =
                                        false

                                    errorMessage =

                                        task.exception
                                            ?.message
                                            ?: "Account creation failed"
                                }
                            }
                    }
                }
            },

            modifier =
                Modifier.fillMaxWidth(),

            enabled =
                !isLoading

        ) {

            Text(

                text =

                    if (isLoading) {

                        "Creating Account..."

                    } else {

                        "Sign Up"
                    }
            )
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        // =================================
        // SIGN IN
        // =================================

        Button(

            onClick =
                onSignInClick,

            modifier =
                Modifier.fillMaxWidth()

        ) {

            Text(
                text =
                    "Already have an account? Sign In"
            )
        }
    }
}

