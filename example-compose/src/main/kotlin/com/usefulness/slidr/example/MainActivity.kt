package com.usefulness.slidr.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(lightColorScheme()) {
                val navController = rememberNavController(SlidableComposeNavigator())

//                NavHost(navController = navController, startDestination = "list") {
//                    composable("list") {
//                        ListView(navController)
//                    }
//                    composable(
//                        route = "details/{sdkInt}",
//                        arguments = listOf(navArgument("sdkInt") { type = NavType.IntType }),
//                        content = { backStackEntry ->
//                            val sdkInt = backStackEntry.arguments?.getInt("sdkInt") ?: return@composable
//                            DetailsView(navController, sdkInt)
//                        },
//                    )
//                }

                SlidableNavHost(navController = navController, startDestination = "list") {
                    slidableComposable("list") {
                        Log.d("jakub", "onCreate: listView ${it.id}")
                        ListView(navController)
                    }
                    slidableComposable(
                        route = "details/{sdkInt}",
                        arguments = listOf(navArgument("sdkInt") { type = NavType.IntType }),
                        content = { backStackEntry ->
                        Log.d("jakub", "onCreate: detail ${backStackEntry.id}")
                            val sdkInt = backStackEntry.arguments?.getInt("sdkInt") ?: return@slidableComposable

                            DetailsView(navController, sdkInt)
                        },
                    )
                }
            }
        }
    }
}
