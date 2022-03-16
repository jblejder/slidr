package com.usefulness.slidr.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.usefulness.slidr.example.model.AndroidOs

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(darkColorScheme()) {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(navController = navController, startDestination = "list") {
                    composable("list") { ListView(navController) }
                    composable(
                        route = "details/{sdkInt}",
                        arguments = listOf(navArgument("sdkInt") { type = NavType.IntType }),
                        content = { backStackEntry ->
                            val sdkInt = backStackEntry.arguments?.getInt("sdkInt") ?: return@composable
                            DetailsView(navController, sdkInt)
                        },
                    )
                }
            }
        }
    }
}
