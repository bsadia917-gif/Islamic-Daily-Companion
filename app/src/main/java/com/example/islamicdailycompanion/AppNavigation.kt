
package com.example.islamicdailycompanion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.remember
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation() {

        val navController = rememberNavController()

        val auth = remember {
            FirebaseAuth.getInstance()
        }

        val startDestination =
            if (auth.currentUser != null) {
                "home"
            } else {
                "welcome"
            }

        // baqi tumhara existing code...



    // =====================================
    // BOTTOM NAVIGATION ITEMS
    // =====================================

    val navItems = listOf(

        BottomNavItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home
        ),

        BottomNavItem(
            route = "quran",
            title = "Quran",
            icon = Icons.Default.MenuBook
        ),

        BottomNavItem(
            route = "duas",
            title = "Duas",
            icon = Icons.Default.Favorite
        ),

        BottomNavItem(
            route = "tasbeeh",
            title = "Tasbeeh",
            icon = Icons.Default.Notifications
        ),

        BottomNavItem(
            route = "history",
            title = "History",
            icon = Icons.Default.History
        )
    )


    // =====================================
    // CURRENT DESTINATION
    // =====================================

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentDestination =
        navBackStackEntry?.destination


    // =====================================
    // SHOW BOTTOM BAR
    // =====================================

    val showBottomBar =
        currentDestination?.route in
                navItems.map {
                    it.route
                }


    // =====================================
    // SCAFFOLD
    // =====================================

    Scaffold(

        bottomBar = {

            if (showBottomBar) {

                NavigationBar {

                    navItems.forEach { item ->

                        NavigationBarItem(

                            selected =
                                currentDestination
                                    ?.hierarchy
                                    ?.any {
                                        it.route ==
                                                item.route
                                    } == true,

                            onClick = {

                                navController.navigate(
                                    item.route
                                ) {

                                    popUpTo(
                                        navController
                                            .graph
                                            .startDestinationId
                                    ) {

                                        saveState =
                                            true
                                    }

                                    launchSingleTop =
                                        true

                                    restoreState =
                                        true
                                }
                            },

                            icon = {

                                Icon(

                                    imageVector =
                                        item.icon,

                                    contentDescription =
                                        item.title
                                )
                            },

                            label = {

                                Text(
                                    text =
                                        item.title
                                )
                            }
                        )
                    }
                }
            }
        }

    ) { innerPadding ->


        // =====================================
        // NAV HOST
        // =====================================


        NavHost(
            navController = navController,

            startDestination = startDestination,

            modifier = Modifier.padding(innerPadding)
        ) {

            // =====================================
            // WELCOME
            // =====================================

            composable("welcome") {

                WelcomeScreen(

                    onSignInClick = {

                        navController.navigate(
                            "signin"
                        )
                    },

                    onSignUpClick = {

                        navController.navigate(
                            "signup"
                        )
                    }
                )
            }


            // =====================================
            // SIGN IN
            // =====================================

            composable("signin") {

                SignInScreen(

                    onSignUpClick = {

                        navController.navigate(
                            "signup"
                        )
                    },

                    onSignInClick = {

                        navController.navigate(
                            "home"
                        ) {

                            popUpTo(
                                "welcome"
                            ) {

                                inclusive =
                                    true
                            }
                        }
                    }
                )
            }


            // =====================================
            // SIGN UP
            // =====================================

            composable("signup") {

                SignUpScreen(

                    onSignInClick = {

                        navController.navigate(
                            "signin"
                        )
                    },

                    onSignUpSuccess = {

                        navController.navigate(
                            "home"
                        ) {

                            popUpTo(
                                "welcome"
                            ) {

                                inclusive =
                                    true
                            }
                        }
                    }
                )
            }




            // =====================================
            // HOME
            // =====================================

            composable("home") {

                HomeScreen(

                    onQuranClick = {

                        navController.navigate(
                            "quran"
                        )
                    },

                    onDuaClick = {

                        navController.navigate(
                            "duas"
                        )
                    },

                    onTasbeehClick = {

                        navController.navigate(
                            "tasbeeh"
                        )
                    },

                    onPrayerClick = {

                        navController.navigate(
                            "prayer"
                        )
                    },

                    onProfileClick = {

                        navController.navigate(
                            "profile"
                        )
                    }
                )
            }


            // =====================================
            // QURAN
            // =====================================

            composable("quran") {

                QuranScreen(

                    onSurahClick = { surahName ->

                        navController.navigate(
                            "surah/${
                                android.net.Uri.encode(
                                    surahName
                                )
                            }"
                        )
                    }
                )
            }


            // =====================================
            // SURAH DETAILS
            // =====================================

            composable(
                "surah/{surahName}"
            ) { backStackEntry ->

                val surahName =
                    backStackEntry
                        .arguments
                        ?.getString(
                            "surahName"
                        )
                        ?: ""

                SurahDetailScreen(

                    surahName =
                        surahName,

                    onBackClick = {

                        navController.popBackStack()
                    }
                )
            }


            // =====================================
            // DUAS
            // =====================================

            composable("duas") {

                DuaScreen(

                    onDuaClick = { duaName ->

                        navController.navigate(
                            "dua/${
                                android.net.Uri.encode(
                                    duaName
                                )
                            }"
                        )
                    }
                )
            }


            // =====================================
            // DUA DETAILS
            // =====================================

            composable(
                "dua/{duaName}"
            ) { backStackEntry ->

                val duaName =
                    backStackEntry
                        .arguments
                        ?.getString(
                            "duaName"
                        )
                        ?: ""

                DuaDetailScreen(

                    duaName =
                        duaName,

                    onBackClick = {

                        navController.popBackStack()
                    }
                )
            }


            // =====================================
            // TASBEEH
            // =====================================

            composable("tasbeeh") {

                TasbeehScreen()
            }


            // =====================================
            // HISTORY MENU
            // =====================================

            composable("history") {

                HistoryMenuScreen(

                    onTasbeehHistoryClick = {

                        navController.navigate(
                            "tasbeeh_history"
                        )
                    },

                    onPrayerHistoryClick = {

                        navController.navigate(
                            "prayer_history"
                        )
                    }
                )
            }


            // =====================================
            // TASBEEH HISTORY
            // =====================================

            composable(
                "tasbeeh_history"
            ) {

                TasbeehHistoryScreen()
            }


            // =====================================
            // PRAYER HISTORY
            // =====================================

            composable(
                "prayer_history"
            ) {

                PrayerHistoryScreen()
            }


            // =====================================
            // PRAYER
            // =====================================

            composable("prayer") {

                PrayerScreen()
            }


            // =====================================
            // PROFILE
            // =====================================

            composable("profile") {

                ProfileScreen(

                    onSettingsClick = {

                        navController.navigate(
                            "settings"
                        )
                    },

                    onAboutClick = {

                        navController.navigate(
                            "about"
                        )
                    },

                    onLogoutClick = {

                        navController.navigate("welcome") {

                            popUpTo(0) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }


            // =====================================
            // SETTINGS
            // =====================================

            composable("settings") {

                SettingsScreen()
            }


            // =====================================
            // ABOUT
            // =====================================

            composable("about") {

                AboutScreen()
            }
        }
    }
}


// =====================================
// BOTTOM NAV ITEM
// =====================================

data class BottomNavItem(

    val route: String,

    val title: String,

    val icon:
    androidx.compose.ui.graphics.vector.ImageVector
)
