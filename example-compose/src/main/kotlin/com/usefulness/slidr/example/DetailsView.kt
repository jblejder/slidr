package com.usefulness.slidr.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
private fun Dupa() {
    DetailsView(rememberNavController(), 15)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailsView(navController: NavHostController, sdkInt: Int) {
    val resources = LocalContext.current.resources
    val item = remember { loadData(resources).first { it.sdkInt == sdkInt } }

    Scaffold(
        Modifier.layoutId("ListView ${counter++}"),
        topBar = { SmallTopAppBar(title = { Text(text = item.name.orEmpty()) }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = item.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                if (item.description != null) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Button(onClick = {
                    navController.navigate("details/${item.sdkInt}")
                }) {
                    Text(text = "ja chce jeszcze raz!")
                }
            }
        },
    )
}
