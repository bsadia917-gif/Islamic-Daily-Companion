
package com.example.islamicdailycompanion

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

// =========================================
// PROFILE SCREEN
// =========================================

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    val context = LocalContext.current

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val user = auth.currentUser

    // =====================================
    // SHARED PREFERENCES
    // =====================================

    val preferences = remember {

        context.getSharedPreferences(
            "user_profile",
            Context.MODE_PRIVATE
        )
    }

    // =====================================
    // EMAIL
    // =====================================

    val userEmail =
        user?.email ?: "No email available"

    // =====================================
    // USER NAME
    // =====================================

    var userName by remember {

        mutableStateOf(
            preferences.getString(
                "user_name",
                user?.displayName ?: "User"
            ) ?: "User"
        )
    }

    // =====================================
    // PROFILE IMAGE URI
    // =====================================

    var profileImageUri by remember {

        mutableStateOf(
            preferences.getString(
                "profile_image_uri",
                null
            )
        )
    }

    // =====================================
    // DIALOG STATES
    // =====================================

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showImageOptions by remember {
        mutableStateOf(false)
    }

    // =====================================
    // IMAGE PICKER
    // =====================================

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->

            if (uri != null) {

                // Save permanent read permission
                try {

                    context.contentResolver
                        .takePersistableUriPermission(
                            uri,
                            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                } catch (_: Exception) {
                    // Some providers do not support this
                }

                profileImageUri =
                    uri.toString()

                preferences.edit()
                    .putString(
                        "profile_image_uri",
                        uri.toString()
                    )
                    .apply()
            }
        }

    // =====================================
    // FIREBASE NAME
    // =====================================

    LaunchedEffect(user?.uid) {

        val firebaseName =
            auth.currentUser?.displayName

        if (!firebaseName.isNullOrEmpty()) {

            userName = firebaseName
        }
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
                .padding(20.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        // =================================
        // TITLE
        // =================================

        Text(
            text = "👤 My Profile",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text = "Manage your profile",
            fontSize = 15.sp
        )

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        // =================================
        // PROFILE CARD
        // =================================

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(24.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
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
                        .padding(22.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                // =================================
                // PROFILE PICTURE
                // =================================

                if (profileImageUri != null) {

                    val bitmap =
                        remember(
                            profileImageUri
                        ) {

                            loadBitmapFromUri(
                                context,
                                Uri.parse(
                                    profileImageUri
                                )
                            )
                        }

                    if (bitmap != null) {

                        Image(

                            bitmap =
                                bitmap.asImageBitmap(),

                            contentDescription =
                                "Profile Picture",

                            modifier =
                                Modifier
                                    .size(110.dp)
                                    .clip(
                                        CircleShape
                                    )
                                    .clickable {

                                        showImageOptions =
                                            true
                                    },

                            contentScale =
                                ContentScale.Crop
                        )

                    } else {

                        DefaultProfileIcon(
                            onClick = {
                                showImageOptions = true
                            }
                        )
                    }

                } else {

                    DefaultProfileIcon(
                        onClick = {
                            showImageOptions = true
                        }
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                TextButton(

                    onClick = {
                        showImageOptions = true
                    }

                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Edit,

                        contentDescription =
                            "Change Picture"
                    )

                    Spacer(
                        modifier =
                            Modifier.width(5.dp)
                    )

                    Text(
                        text =
                            "Change Picture"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                // =================================
                // NAME
                // =================================

                Text(

                    text =
                        userName,

                    fontSize =
                        24.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                // =================================
                // EMAIL
                // =================================

                Text(

                    text =
                        userEmail,

                    fontSize =
                        15.sp,

                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                // =================================
                // EDIT PROFILE
                // =================================

                Button(

                    onClick = {
                        showEditDialog = true
                    },

                    shape =
                        RoundedCornerShape(14.dp)
                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Edit,

                        contentDescription =
                            "Edit Profile"
                    )

                    Spacer(
                        modifier =
                            Modifier.width(7.dp)
                    )

                    Text(
                        text =
                            "Edit Profile"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        // =================================
        // ACCOUNT
        // =================================

        ProfileSectionTitle(
            title = "Account"
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        ProfileOptionCard(

            icon =
                Icons.Default.AccountCircle,

            title =
                "Profile Information",

            subtitle =
                "Your name and email address",

            onClick = {
                showEditDialog = true
            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // =================================
        // APP SETTINGS
        // =================================

        ProfileSectionTitle(
            title = "App Settings"
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        ProfileOptionCard(

            icon =
                Icons.Default.Notifications,

            title =
                "Notifications",

            subtitle =
                "Manage prayer reminders",

            onClick = {
                // Notifications will be connected later
            }
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        ProfileOptionCard(

            icon =
                Icons.Default.Settings,

            title =
                "Settings",

            subtitle =
                "Manage application settings",

            onClick =
                onSettingsClick
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // =================================
        // ABOUT
        // =================================

        ProfileSectionTitle(
            title = "About"
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        ProfileOptionCard(

            icon =
                Icons.Default.Info,

            title =
                "About",

            subtitle =
                "About Islamic Daily Companion",

            onClick =
                onAboutClick
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        ProfileOptionCard(

            icon =
                Icons.Default.Info,

            title =
                "Islamic Daily Companion",

            subtitle =
                "Version 1.0",

            onClick = {
                // App information
            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // =================================
        // LOGOUT
        // =================================

        ProfileOptionCard(

            icon =
                Icons.Default.Logout,

            title =
                "Logout",

            subtitle =
                "Sign out from your account",

            onClick = {
                showLogoutDialog = true
            }
        )

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

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
                Modifier.height(3.dp)
        )

        Text(
            text =
                "May Allah make every day better 🤲",

            fontSize =
                13.sp
        )
    }

    // =====================================
    // PROFILE PICTURE DIALOG
    // =====================================

    if (showImageOptions) {

        AlertDialog(

            onDismissRequest = {
                showImageOptions = false
            },

            title = {
                Text(
                    text =
                        "Profile Picture"
                )
            },

            text = {
                Text(
                    text =
                        "Choose an option"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        showImageOptions =
                            false

                        imagePickerLauncher.launch(
                            arrayOf("image/*")
                        )
                    }
                ) {

                    Text(
                        text =
                            "Choose Picture"
                    )
                }
            },

            dismissButton = {

                Row {

                    if (profileImageUri != null) {

                        TextButton(

                            onClick = {

                                profileImageUri =
                                    null

                                preferences.edit()
                                    .remove(
                                        "profile_image_uri"
                                    )
                                    .apply()

                                showImageOptions =
                                    false
                            }
                        ) {

                            Icon(

                                imageVector =
                                    Icons.Default.Delete,

                                contentDescription =
                                    "Remove Picture"
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(4.dp)
                            )

                            Text(
                                text =
                                    "Remove"
                            )
                        }
                    }

                    TextButton(

                        onClick = {
                            showImageOptions =
                                false
                        }
                    ) {

                        Text(
                            text =
                                "Cancel"
                        )
                    }
                }
            }
        )
    }

    // =====================================
    // EDIT PROFILE DIALOG
    // =====================================

    if (showEditDialog) {

        EditProfileDialog(

            currentName =
                userName,

            onDismiss = {
                showEditDialog = false
            },

            onSave = { newName ->

                userName =
                    newName

                preferences.edit()
                    .putString(
                        "user_name",
                        newName
                    )
                    .apply()

                auth.currentUser
                    ?.updateProfile(

                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(
                                newName
                            )
                            .build()
                    )

                showEditDialog =
                    false
            }
        )
    }

    // =====================================
    // LOGOUT DIALOG
    // =====================================

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog =
                    false
            },

            title = {
                Text(
                    text =
                        "Logout"
                )
            },

            text = {
                Text(
                    text =
                        "Are you sure you want to logout?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        auth.signOut()

                        showLogoutDialog =
                            false

                        onLogoutClick()
                    }
                ) {

                    Text(
                        text =
                            "Logout"
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showLogoutDialog =
                            false
                    }
                ) {

                    Text(
                        text =
                            "Cancel"
                    )
                }
            }
        )
    }
}


// =========================================
// DEFAULT PROFILE ICON
// =========================================

@Composable
fun DefaultProfileIcon(
    onClick: () -> Unit
) {

    Icon(

        imageVector =
            Icons.Default.AccountCircle,

        contentDescription =
            "Profile Picture",

        modifier =
            Modifier
                .size(110.dp)
                .clip(CircleShape)
                .clickable {
                    onClick()
                },

        tint =
            Color(0xFF388E3C)
    )
}


// =========================================
// LOAD BITMAP FROM URI
// =========================================

fun loadBitmapFromUri(
    context: Context,
    uri: Uri
): Bitmap? {

    return try {

        context.contentResolver
            .openInputStream(uri)
            ?.use { inputStream ->

                BitmapFactory.decodeStream(
                    inputStream
                )
            }

    } catch (
        e: Exception
    ) {

        null
    }
}


// =========================================
// SECTION TITLE
// =========================================

@Composable
fun ProfileSectionTitle(
    title: String
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                title,

            fontSize =
                20.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// =========================================
// OPTION CARD
// =========================================

@Composable
fun ProfileOptionCard(

    icon: ImageVector,

    title: String,

    subtitle: String,

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
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            ),

        onClick =
            onClick
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(17.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(

                imageVector =
                    icon,

                contentDescription =
                    title,

                modifier =
                    Modifier.size(32.dp),

                tint =
                    Color(0xFF388E3C)
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
                        17.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(

                    text =
                        subtitle,

                    fontSize =
                        13.sp,

                    color =
                        Color.Gray
                )
            }
        }
    }
}


// =========================================
// EDIT PROFILE DIALOG
// =========================================

@Composable
fun EditProfileDialog(

    currentName: String,

    onDismiss: () -> Unit,

    onSave: (String) -> Unit

) {

    var name by remember {

        mutableStateOf(
            currentName
        )
    }

    AlertDialog(

        onDismissRequest =
            onDismiss,

        title = {

            Text(
                text =
                    "Edit Profile"
            )
        },

        text = {

            OutlinedTextField(

                value =
                    name,

                onValueChange = {

                    name = it
                },

                label = {

                    Text(
                        text =
                            "Your Name"
                    )
                },

                singleLine = true,

                modifier =
                    Modifier.fillMaxWidth()
            )
        },

        confirmButton = {

            TextButton(

                onClick = {

                    val finalName =
                        name.trim()

                    if (
                        finalName.isNotEmpty()
                    ) {

                        onSave(
                            finalName
                        )
                    }
                }
            ) {

                Text(
                    text =
                        "Save"
                )
            }
        },

        dismissButton = {

            TextButton(
                onClick =
                    onDismiss
            ) {

                Text(
                    text =
                        "Cancel"
                )
            }
        }
    )
}

