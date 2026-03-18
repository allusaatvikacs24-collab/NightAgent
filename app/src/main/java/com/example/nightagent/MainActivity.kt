package com.example.nightagent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nightagent.navigation.NavGraph
import com.example.nightagent.ui.theme.NightagentTheme
import com.example.nightagent.ui.viewmodels.LocationViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NightagentTheme {
                NavGraph()

            }
        }
    }
}
