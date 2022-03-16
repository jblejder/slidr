package com.usefulness.slidr.example

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
private fun Dupa() {
    DetailsView(rememberNavController(), 23)
}

@Composable
internal fun DetailsView(navController: NavHostController, sdkInt: Int) {
    val resources = LocalContext.current.resources
    val content = remember { loadData(resources).first { it.sdkInt == sdkInt } }
    Text(text = content.toString())
}
